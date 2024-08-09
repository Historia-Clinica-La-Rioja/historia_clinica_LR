package net.pladema.emergencycare.servicerequest.service.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.service.domain.ExtendedServiceRequestBo;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.emergencycare.servicerequest.service.CreateEmergencyCareServiceRequestService;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateEmergencyCareServiceRequestImpl implements CreateEmergencyCareServiceRequestService {

	private final CreateServiceRequestService createServiceRequestService;

	@Override
	public Integer execute(ExtendedServiceRequestBo serviceRequest, Integer episodeId) {
		log.debug("Input parameter -> serviceRequest {}, episodeId {}", serviceRequest, episodeId);
		ServiceRequestBo serviceRequestBo = mapToServiceRequestBo(serviceRequest, episodeId);
		Integer result = createServiceRequestService.execute(serviceRequestBo);
		log.debug("Output -> {}", result);
		return result;
	}

	private ServiceRequestBo mapToServiceRequestBo(ExtendedServiceRequestBo extendedServiceRequestBo, Integer episodeId) {
		return ServiceRequestBo.builder()
				.patientInfo(extendedServiceRequestBo.getPatientInfo())
				.categoryId(extendedServiceRequestBo.getCategoryId())
				.institutionId(extendedServiceRequestBo.getInstitutionId())
				.doctorId(extendedServiceRequestBo.getDoctorId())
				.diagnosticReports(extendedServiceRequestBo.getDiagnosticReports())
				.noteId(extendedServiceRequestBo.getNoteId())
				.requestDate(extendedServiceRequestBo.getRequestDate())
				.associatedSourceTypeId(SourceType.EMERGENCY_CARE)
				.medicalCoverageId(extendedServiceRequestBo.getMedicalCoverageId())
				.associatedSourceId(episodeId)
				.observations(extendedServiceRequestBo.getObservations())
				.studyTypeId(extendedServiceRequestBo.getStudyTypeId())
				.requiresTechnician(extendedServiceRequestBo.getRequiresTechnician())
				.build();
	}

}
