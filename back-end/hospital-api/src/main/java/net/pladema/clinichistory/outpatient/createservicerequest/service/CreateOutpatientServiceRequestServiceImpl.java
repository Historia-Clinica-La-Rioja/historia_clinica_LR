package net.pladema.clinichistory.outpatient.createservicerequest.service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.application.port.output.ServiceRequestTemplatePort;
import net.pladema.clinichistory.requests.service.domain.ExtendedServiceRequestBo;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateOutpatientServiceRequestServiceImpl implements CreateOutpatientServiceRequestService {

	private final CreateServiceRequestService createServiceRequestService;

	private final ServiceRequestTemplatePort serviceRequestTemplatePort;

	@Override
	public Integer execute(ExtendedServiceRequestBo extendedServiceRequestBo) {
		log.debug("Input parameter -> outpatientServiceRequestBo {}", extendedServiceRequestBo);
		ServiceRequestBo serviceRequestBo = mapToServiceRequestBo(extendedServiceRequestBo);
		Integer result = createServiceRequestService.execute(serviceRequestBo);
		serviceRequestTemplatePort.saveAll(result, extendedServiceRequestBo.getTemplateIds());
		log.debug("Output -> {}", result);
		return result;
	}

	private ServiceRequestBo mapToServiceRequestBo(ExtendedServiceRequestBo extendedServiceRequestBo) {
		return ServiceRequestBo.builder()
				.patientInfo(extendedServiceRequestBo.getPatientInfo())
				.categoryId(extendedServiceRequestBo.getCategoryId())
				.institutionId(extendedServiceRequestBo.getInstitutionId())
				.doctorId(extendedServiceRequestBo.getDoctorId())
				.diagnosticReports(extendedServiceRequestBo.getDiagnosticReports())
				.noteId(extendedServiceRequestBo.getNoteId())
				.requestDate(extendedServiceRequestBo.getRequestDate())
				.associatedSourceTypeId(SourceType.OUTPATIENT)
				.medicalCoverageId(extendedServiceRequestBo.getMedicalCoverageId())
				.observations(extendedServiceRequestBo.getObservations())
				.studyTypeId(extendedServiceRequestBo.getStudyTypeId())
				.build();
	}

}
