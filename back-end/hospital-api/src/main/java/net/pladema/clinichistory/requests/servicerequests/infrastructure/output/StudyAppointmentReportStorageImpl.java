package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;


import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import ar.lamansys.sgh.clinichistory.application.createDocument.DocumentFactory;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ConclusionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentReportSnomedConceptRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentReportSnomedConcept;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.StudyAppointmentReportStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.InformerObservationBo;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyAppointmentBo;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EDiagnosticImageReportStatus;
import net.pladema.clinichistory.requests.servicerequests.service.DiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.ListTranscribedDiagnosticReportInfoService;
import net.pladema.imagenetwork.derivedstudies.service.MoveStudiesService;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.DetailsOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.entity.DetailsOrderImage;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.user.controller.dto.UserPersonDto;
import net.pladema.user.repository.UserPersonRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudyAppointmentReportStorageImpl implements StudyAppointmentReportStorage {

	private final AppointmentRepository appointmentRepository;
	private final AppointmentOrderImageRepository appointmentOrderImageRepository;
	private final DocumentReportSnomedConceptRepository documentReportSnomedConceptRepository;
	private final FeatureFlagsService featureFlagsService;
	private final DetailsOrderImageRepository detailsOrderImageRepository;
	private final SnomedService snomedService;
	private final DocumentService documentService;
	private final NoteService noteService;
	private final UserPersonRepository userPersonRepository;
	private final PersonExternalService personExternalService;
	private final PatientExternalService patientExternalService;
	private final DocumentFactory documentFactory;
	private final DateTimeProvider dateTimeProvider;
	private final SharedDocumentPort sharedDocumentPort;
	private final DiagnosticReportInfoService diagnosticReportInfoService;
	private final ListTranscribedDiagnosticReportInfoService transcribedDiagnosticReportInfoService;
	private final MoveStudiesService moveStudiesService;

	@Override
	public StudyAppointmentBo getStudyByAppointment(Integer appointmentId) {
		log.debug("Get study by appointmentId {}", appointmentId);

		StudyAppointmentBo result = appointmentRepository.getPatientInfoByAppointmentId(appointmentId);

		result.setPatientFullName(result.getFullName(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)));

		Optional<Long> reportDocumentId = appointmentOrderImageRepository.getReportDocumentIdByAppointmentId(appointmentId);

		if (reportDocumentId.isPresent()) {
			Long documentId = reportDocumentId.get();
			var obs = new InformerObservationBo();
			documentService.findById(documentId).ifPresent( d -> {
				if (d.getEvolutionNoteId() != null)
					obs.setEvolutionNote(noteService.getDescriptionById(d.getEvolutionNoteId()));
				obs.setActionTime(d.getUpdatedOn());
				obs.setCreatedBy(getFullNameByUserId(d.getCreatedBy()));
				obs.setConfirmed(d.getStatusId().equals(DocumentStatus.FINAL));
				obs.setId(d.getId());
			});

			obs.setConclusions(documentService.getConclusionsFromDocument(documentId));
			result.setInformerObservations(obs);

			if(obs.isConfirmed()) {
				result.setStatusId(EDiagnosticImageReportStatus.COMPLETED.getId());
				result.setActionTime(obs.getActionTime());
			}
			else {
				result.setStatusId(EDiagnosticImageReportStatus.PENDING.getId());
				Optional<DetailsOrderImage> doi = detailsOrderImageRepository.findById(appointmentId);
				doi.ifPresent(detailsOrderImage -> result.setActionTime(detailsOrderImage.getCompletedOn()));
			}
		}
		else {
			result.setStatusId(EDiagnosticImageReportStatus.PENDING.getId());
			Optional<DetailsOrderImage> doi = detailsOrderImageRepository.findById(appointmentId);
			doi.ifPresent(detailsOrderImage -> result.setActionTime(detailsOrderImage.getCompletedOn()));
		}

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

	private void assertConclusionsIsNotEmptyAndNull(List<ConclusionBo> conclusions) {
		Assert.notNull(conclusions, "Es obligatorio que se agregue al menos una conclusión");
		Assert.isTrue(!conclusions.isEmpty(), "Es obligatorio que se agregue al menos una conclusión");
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
		if (originInstitutionId != null && originInstitutionId != obs.getInstitutionId())
			obs.setInstitutionId(originInstitutionId);

		obs.setDiagnosticReports(diagnosticReportInfoService.getByAppointmentId(appointmentId) != null ? List.of(diagnosticReportInfoService.getByAppointmentId(appointmentId)) : null);
		obs.setTranscribedDiagnosticReport(transcribedDiagnosticReportInfoService.getByAppointmentId(appointmentId));

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

	private String getFullNameByUserId(Integer userId) {
		Optional<UserPersonDto> informer = userPersonRepository.getPersonIdByUserId(userId)
				.map(personExternalService::getUserPersonInformation);

		String fullName;

		if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && informer.get().getNameSelfDetermination() != null) {
			fullName = informer.get().getNameSelfDetermination() + " " + informer.get().getLastName();
		} else {
			fullName = informer.get().getFirstName();
			if (informer.get().getMiddleNames() != null)
				fullName += " " + informer.get().getMiddleNames();

			fullName += " " + informer.get().getLastName();
		}
		if (informer.get().getOthersLastNames() != null)
			fullName += " " + informer.get().getOthersLastNames();

		return fullName;
	}

}