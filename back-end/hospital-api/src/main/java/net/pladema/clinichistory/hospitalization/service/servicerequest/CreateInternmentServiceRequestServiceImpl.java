package net.pladema.clinichistory.hospitalization.service.servicerequest;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentPatientService;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentEpisodeProcessBo;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentServiceRequestBo;

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

	@Override
	public Integer execute(InternmentServiceRequestBo internmentServiceRequestBo) {
		log.debug("Input parameter -> internmentServiceRequestBo {}", internmentServiceRequestBo);
		Integer activeEpisodeId = getActiveInternmentEpisodeId(internmentServiceRequestBo.getInstitutionId(), internmentServiceRequestBo.getPatientId());
		ServiceRequestBo serviceRequestBo = mapToServiceRequestBo(internmentServiceRequestBo, activeEpisodeId);
		Integer result = createServiceRequestService.execute(serviceRequestBo);
		log.debug("Output -> {}", result);
		return result;
	}

	private ServiceRequestBo mapToServiceRequestBo(InternmentServiceRequestBo internmentServiceRequestBo, Integer activeEpisodeId) {
		return ServiceRequestBo.builder()
				.patientInfo(internmentServiceRequestBo.getPatientInfo())
				.categoryId(internmentServiceRequestBo.getCategoryId())
				.institutionId(internmentServiceRequestBo.getInstitutionId())
				.doctorId(internmentServiceRequestBo.getDoctorId())
				.diagnosticReports(internmentServiceRequestBo.getDiagnosticReports())
				.noteId(internmentServiceRequestBo.getNoteId())
				.requestDate(internmentServiceRequestBo.getRequestDate())
				.associatedSourceTypeId(SourceType.HOSPITALIZATION)
				.associatedSourceId(activeEpisodeId)
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
