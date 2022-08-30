package net.pladema.clinichistory.outpatient.createservicerequest.service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.createservicerequest.service.domain.OutpatientServiceRequestBo;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateOutpatientServiceRequestServiceImpl implements CreateOutpatientServiceRequestService {

	private final CreateServiceRequestService createServiceRequestService;

	@Override
	public Integer execute(OutpatientServiceRequestBo outpatientServiceRequestBo) {
		log.debug("Input parameter -> outpatientServiceRequestBo {}", outpatientServiceRequestBo);
		ServiceRequestBo serviceRequestBo = mapToServiceRequestBo(outpatientServiceRequestBo);
		Integer result = createServiceRequestService.execute(serviceRequestBo);
		log.debug("Output -> {}", result);
		return result;
	}

	private ServiceRequestBo mapToServiceRequestBo(OutpatientServiceRequestBo outpatientServiceRequestBo) {
		return ServiceRequestBo.builder()
				.patientInfo(outpatientServiceRequestBo.getPatientInfo())
				.categoryId(outpatientServiceRequestBo.getCategoryId())
				.institutionId(outpatientServiceRequestBo.getInstitutionId())
				.doctorId(outpatientServiceRequestBo.getDoctorId())
				.diagnosticReports(outpatientServiceRequestBo.getDiagnosticReports())
				.noteId(outpatientServiceRequestBo.getNoteId())
				.requestDate(outpatientServiceRequestBo.getRequestDate())
				.associatedSourceTypeId(SourceType.OUTPATIENT)
				.medicalCoverageId(outpatientServiceRequestBo.getMedicalCoverageId())
				.build();
	}

}
