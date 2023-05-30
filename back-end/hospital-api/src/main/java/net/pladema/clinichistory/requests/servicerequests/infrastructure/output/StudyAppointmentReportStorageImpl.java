package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;


import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.servicerequests.application.port.StudyAppointmentReportStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.InformerObservationBo;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyAppointmentBo;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service.EInformerWorklistStatus;

import net.pladema.clinichistory.requests.servicerequests.infrastructure.output.ReportSnomedConcept.ReportSnomedConceptRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentOrderImageRepository;
import net.pladema.medicalconsultation.appointment.repository.AppointmentRepository;


import net.pladema.medicalconsultation.appointment.repository.DetailsOrderImageRepository;

import net.pladema.medicalconsultation.appointment.repository.entity.DetailsOrderImage;
import net.pladema.medicalconsultation.appointment.repository.entity.DetailsOrderImagePK;
import net.pladema.permissions.repository.enums.ERole;

import net.pladema.person.controller.service.PersonExternalService;
import net.pladema.user.controller.dto.UserPersonDto;

import net.pladema.user.repository.UserPersonRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudyAppointmentReportStorageImpl implements StudyAppointmentReportStorage {

	private final AppointmentRepository appointmentRepository;
	private final AppointmentOrderImageRepository appointmentOrderImageRepository;
	private final ReportSnomedConceptRepository reportSnomedConceptRepository;
	private final FeatureFlagsService featureFlagsService;
	private final DetailsOrderImageRepository detailsOrderImageRepository;
	private final SnomedService snomedService;
	private final DocumentService documentService;
	private final UserPersonRepository userPersonRepository;
	private final PersonExternalService personExternalService;
	private final NoteService noteService;
	
	@Override
	public StudyAppointmentBo getStudyByAppointment(Integer appointmentId) {
		log.debug("Get study by appointmentId {}", appointmentId);

		StudyAppointmentBo result = appointmentRepository.getPatientInfoByAppointmentId(appointmentId);

		result.setPatientFullName(result.getFullName(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)));

		Optional<Long> reportDocumentId = appointmentOrderImageRepository.getReportDocumentIdByAppointmentId(appointmentId);

		if (reportDocumentId.isPresent()) {
			var obs = new InformerObservationBo();
			documentService.findById(reportDocumentId.get()).ifPresent( d -> {
				if (d.getEvolutionNoteId() != null)
					obs.setEvolutionNote(noteService.getDescriptionById(d.getEvolutionNoteId()));
				obs.setActionTime(d.getUpdatedOn());
				obs.setCreatedBy(getFullNameByUserId(d.getCreatedBy()));
				obs.setConfirmed(d.getStatusId().equals(DocumentStatus.FINAL));
				obs.setId(d.getId());
			});

			obs.setProblems(reportSnomedConceptRepository.getSnomedConceptsByReportDocumentId(obs.getId())
					.stream().map(snomedService::getSnomed).map(ProblemBo::new).collect(Collectors.toList()));
			result.setInformerObservations(obs);

			if(obs.isConfirmed()) {
				result.setStatusId(EInformerWorklistStatus.COMPLETED.getId());
				result.setActionTime(obs.getActionTime());
			}
			else {
				result.setStatusId(EInformerWorklistStatus.PENDING.getId());
				Optional<DetailsOrderImage> doi = detailsOrderImageRepository.findById(new DetailsOrderImagePK(appointmentId, ERole.TECNICO.getId()));
				if (doi.isPresent())
					result.setActionTime(doi.get().getCompletedOn());
			}
		}
		else {
			result.setStatusId(EInformerWorklistStatus.PENDING.getId());
			Optional<DetailsOrderImage> doi = detailsOrderImageRepository.findById(new DetailsOrderImagePK(appointmentId, ERole.TECNICO.getId()));
			if (doi.isPresent())
				result.setActionTime(doi.get().getCompletedOn());
		}

		log.debug("Output -> {}", result);
		return result;
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