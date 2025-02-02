package net.pladema.clinichistory.hospitalization.service.servicerequest;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.application.port.output.ServiceRequestTemplatePort;
import net.pladema.clinichistory.hospitalization.service.InternmentPatientService;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeProcessBo;
import net.pladema.clinichistory.requests.service.domain.GenericServiceRequestBo;

import net.pladema.clinichistory.hospitalization.service.servicerequest.exception.CreateInternmentServiceRequestEnumException;
import net.pladema.clinichistory.hospitalization.service.servicerequest.exception.CreateInternmentServiceRequestException;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;

import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateInternmentServiceRequestServiceImpl implements CreateInternmentServiceRequestService {

	private final CreateServiceRequestService createServiceRequestService;
	private final InternmentPatientService internmentPatientService;

	private final ServiceRequestTemplatePort serviceRequestTemplatePort;


	@Override
	public Integer execute(GenericServiceRequestBo genericServiceRequestBo) {
		log.debug("Input parameter -> internmentServiceRequestBo {}", genericServiceRequestBo);
		Integer activeEpisodeId = getActiveInternmentEpisodeId(genericServiceRequestBo.getInstitutionId(), genericServiceRequestBo.getPatientId());
		ServiceRequestBo serviceRequestBo = mapToServiceRequestBo(genericServiceRequestBo, activeEpisodeId);
		Integer result = createServiceRequestService.execute(serviceRequestBo);
		serviceRequestTemplatePort.saveAll(result, genericServiceRequestBo.getTemplateIds());
		log.debug("Output -> {}", result);
		return result;
	}

	private ServiceRequestBo mapToServiceRequestBo(GenericServiceRequestBo genericServiceRequestBo, Integer activeEpisodeId) {
		return ServiceRequestBo.builder()
				.patientInfo(genericServiceRequestBo.getPatientInfo())
				.categoryId(genericServiceRequestBo.getCategoryId())
				.institutionId(genericServiceRequestBo.getInstitutionId())
				.doctorId(genericServiceRequestBo.getDoctorId())
				.diagnosticReports(genericServiceRequestBo.getDiagnosticReports())
				.noteId(genericServiceRequestBo.getNoteId())
				.requestDate(genericServiceRequestBo.getRequestDate())
				.associatedSourceTypeId(SourceType.HOSPITALIZATION)
				.associatedSourceId(activeEpisodeId)
				.observations(genericServiceRequestBo.getObservations())
				.studyTypeId(genericServiceRequestBo.getStudyTypeId())
				.requiresTransfer(genericServiceRequestBo.getRequiresTransfer())
				.deferredDate(genericServiceRequestBo.getDeferredDate())
				.build();
	}

	private Integer getActiveInternmentEpisodeId(Integer institutionId, Integer patientId) {
		log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		InternmentEpisodeProcessBo internmentEpisodeProcessBo = internmentPatientService.internmentEpisodeInProcess(institutionId, patientId);
		if (internmentEpisodeProcessBo.getId() == null || !internmentEpisodeProcessBo.isInProgress())
			throw new CreateInternmentServiceRequestException(CreateInternmentServiceRequestEnumException.ACTIVE_INTERNMENT_EPISODE_NOT_EXISTS,
					"No existe episodio de internación activo del paciente con ID " + patientId + " en la institución " + institutionId);
		Integer result = internmentEpisodeProcessBo.getId();
		log.debug("Output -> {}", result);
		return result;
	}
}
