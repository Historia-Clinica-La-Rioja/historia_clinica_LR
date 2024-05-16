package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;


import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentReportSnomedConceptRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentReportSnomedConcept;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.StudyAppointmentReportStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.InformerObservationBo;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyAppointmentBo;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;
import net.pladema.clinichistory.requests.servicerequests.service.DiagnosticReportInfoService;
import net.pladema.clinichistory.requests.transcribed.application.getbyappointmentid.GetTranscribedServiceRequestByAppointmentId;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import net.pladema.imagenetwork.derivedstudies.service.MoveStudiesService;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.DetailsOrderImageRepository;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.person.service.PersonService;
import net.pladema.user.repository.UserPersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudyAppointmentReportStorageImpl implements StudyAppointmentReportStorage {

	private final AppointmentRepository appointmentRepository;
	private final AppointmentOrderImageRepository appointmentOrderImageRepository;
	private final DocumentReportSnomedConceptRepository documentReportSnomedConceptRepository;
	private final DetailsOrderImageRepository detailsOrderImageRepository;
	private final SnomedService snomedService;
	private final DocumentService documentService;
	private final NoteService noteService;
	private final UserPersonRepository userPersonRepository;
	private final PatientExternalService patientExternalService;
	private final DocumentFactory documentFactory;
	private final SharedDocumentPort sharedDocumentPort;
	private final DiagnosticReportInfoService diagnosticReportInfoService;
	private final GetTranscribedServiceRequestByAppointmentId getTranscribedServiceRequestByAppointmentId;
	private final PersonService personService;
	private final MoveStudiesService moveStudiesService;
	private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;

	@Override
	public StudyAppointmentBo getStudyByAppointment(Integer appointmentId) {
		log.debug("Get study by appointmentId {}", appointmentId);

		StudyAppointmentBo result = appointmentRepository.getCompletionInformationAboutStudy(appointmentId);

		result.setPatientFullName(personService.getCompletePersonNameById(result.getPersonId()));

		appointmentOrderImageRepository.getReportDocumentIdByAppointmentId(appointmentId)
			.ifPresent(documentId -> {
				var obs = new InformerObservationBo();
				documentService.findById(documentId).ifPresent(d -> {
					obs.setId(d.getId());
					if (d.getEvolutionNoteId() != null)
						obs.setEvolutionNote(noteService.getDescriptionById(d.getEvolutionNoteId()));

					userPersonRepository.getPersonIdByUserId(d.getCreatedBy())
									.ifPresent(informerPersonId -> obs.setCreatedBy(personService.getCompletePersonNameById(informerPersonId)));

					obs.setConfirmed(d.getStatusId().equals(DocumentStatus.FINAL));
					obs.setActionTime(d.getUpdatedOn());

					if (obs.isConfirmed())
						result.setStatusId(EDiagnosticImageReportStatus.COMPLETED.getId());
				});

				obs.setConclusions(documentService.getConclusionsFromDocument(documentId));
				result.setInformerObservations(obs);
				});

		moveStudiesService.getSizeImageByAppointmentId(appointmentId)
				.ifPresent(result::setSizeImage);

		appointmentOrderImageRepository.getIdImage(appointmentId).ifPresent(studyInstanceUID -> {
			result.setIsAvailableInPACS(getPacWhereStudyIsHosted.run(studyInstanceUID, false).isAvailableInPACS());
			result.setImageId(studyInstanceUID);
		});

		log.debug("Output -> {}", result);
		return result;
	}


	@Override
	@Transactional
	public Long createDraftReport(Integer appointmentId, InformerObservationBo informerObservations) {
		log.debug("Input parameters -> appointmentId {}, informerObservations {}", appointmentId, informerObservations);

		assertEvolutionNoteIsNotNull(informerObservations.getEvolutionNote());

		Long result = setRequiredFieldsAndSaveDocument(appointmentId, informerObservations, false);

		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	@Transactional
	public Long updateDraftReport(Integer appointmentId, InformerObservationBo informerObservations) {
		log.debug("Input parameters -> appointmentId {}, informerObservations {}", appointmentId, informerObservations);

		assertEvolutionNoteIsNotNull(informerObservations.getEvolutionNote());

		Optional<Long> reportDocumentId = appointmentOrderImageRepository.getReportDocumentIdByAppointmentId(appointmentId);

		if (reportDocumentId.isPresent()) {
			sharedDocumentPort.deleteDocument(reportDocumentId.get(), DocumentStatus.ERROR);
			deletedOldSnomedConcepts(reportDocumentId.get());
		}

		Long result = setRequiredFieldsAndSaveDocument(appointmentId, informerObservations, false);
		log.debug("Output -> {}", result);
		return result;

	}

	@Override
	@Transactional
	public Long closeDraftReport(Integer appointmentId, InformerObservationBo informerObservations) {
		log.debug("Input parameters -> appointmentId {}, informerObservations {}", appointmentId, informerObservations);

		assertEvolutionNoteIsNotNull(informerObservations.getEvolutionNote());

		Optional<Long> reportDocumentId = appointmentOrderImageRepository.getReportDocumentIdByAppointmentId(appointmentId);

		if (reportDocumentId.isPresent()) {
			sharedDocumentPort.deleteDocument(reportDocumentId.get(), DocumentStatus.ERROR);
			deletedOldSnomedConcepts(reportDocumentId.get());
		}

		Long result = setRequiredFieldsAndSaveDocument(appointmentId, informerObservations, true);
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public Long saveReport(Integer appointmentId, InformerObservationBo informerObservations) {
		log.debug("Input parameters -> appointmentId {}, informerObservations {}", appointmentId, informerObservations);

		assertEvolutionNoteIsNotNull(informerObservations.getEvolutionNote());

		Long result = setRequiredFieldsAndSaveDocument(appointmentId, informerObservations, true);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertEvolutionNoteIsNotNull(String evolutionNote) {
		Assert.notNull(evolutionNote, "Las observaciones son obligatorias");
	}

	private void saveSnomedConceptReport(Long id, List<ConclusionBo> conclusions) {
		if (conclusions != null)
			conclusions.forEach(conclusion -> {
				DocumentReportSnomedConcept rsc = new DocumentReportSnomedConcept(
						id,
						snomedService.getSnomedId(conclusion.getSnomed())
								.orElseGet(() -> snomedService.createSnomedTerm(conclusion.getSnomed())));
				documentReportSnomedConceptRepository.save(rsc);
			});
	}

	private Long setRequiredFieldsAndSaveDocument(Integer appointmentId, InformerObservationBo obs, boolean createFile) {
		obs.setEncounterId(appointmentId);
		obs.setConfirmed(createFile);

		Integer originInstitutionId =moveStudiesService.getInstitutionByAppointmetId(appointmentId);
		if (originInstitutionId != null && !originInstitutionId.equals(obs.getInstitutionId()))
			obs.setInstitutionId(originInstitutionId);

		obs.setDiagnosticReports(this.getDiagnosticReports(appointmentId));

		obs.setPatientId(appointmentRepository.getPatientByAppointmentId(appointmentId));
		BasicPatientDto bpd = patientExternalService.getBasicDataFromPatient(obs.getPatientId());
		obs.setPatientInfo(new PatientInfoBo(bpd.getId(), bpd.getPerson().getGender().getId(), bpd.getPerson().getAge(),bpd.getIdentificationType(), bpd.getIdentificationNumber()));

		obs.setPerformedDate(detailsOrderImageRepository.getCompletedDateByAppointmentId(appointmentId));

		Long documentId = documentFactory.run(obs, createFile);

		appointmentOrderImageRepository.setReportDocumentId(appointmentId, documentId);
		appointmentOrderImageRepository.updateReportStatusId(appointmentId, EDiagnosticImageReportStatus.COMPLETED.getId());

		saveSnomedConceptReport(documentId, obs.getConclusions());

		return documentId;
	}

	private void deletedOldSnomedConcepts(Long reportDocumentId) {
		documentReportSnomedConceptRepository.deleteByReportDocumentId(reportDocumentId);
	}

	private List<DiagnosticReportBo> getDiagnosticReports(Integer appointmentId) {

		DiagnosticReportBo diagnosticReportFromOrder = diagnosticReportInfoService.getByAppointmentId(appointmentId);
		if (diagnosticReportFromOrder != null)
			return List.of(diagnosticReportFromOrder);

		List<DiagnosticReportBo> transcribedDiagnosticReports = getTranscribedServiceRequestByAppointmentId.run(appointmentId)
				.map(TranscribedServiceRequestBo::getDiagnosticReports)
				.orElse(List.of());
		if (!transcribedDiagnosticReports.isEmpty())
			return transcribedDiagnosticReports;

		return List.of();
	}

}