package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.shared;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedServiceRequestPort;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceStudyDto;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceServiceRequestProcedureDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.patient.controller.service.PatientExternalService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class SharedServiceRequestPortImpl implements SharedServiceRequestPort {

	private final PatientExternalService patientExternalService;

	private final CreateServiceRequestService createServiceRequestService;

	private final ServiceRequestStorage serviceRequestStorage;

	@Override
	public Integer create(CompleteReferenceStudyDto study) {
		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(study.getPatientId());
		ServiceRequestBo serviceRequest = mapToServiceRequestBo(study, patientDto);
		return createServiceRequestService.execute(serviceRequest);
	}

	@Override
	public List<ReferenceServiceRequestProcedureDto> getProceduresByServiceRequestIds(List<Integer> serviceRequestIds) {
		var servicesRequestsProcedures =  serviceRequestStorage.getProceduresByServiceRequestIds(serviceRequestIds);
		var result = servicesRequestsProcedures.stream().map(sp -> new ReferenceServiceRequestProcedureDto(sp.getServiceRequestId(),
						new SharedSnomedDto(sp.getProcedure().getId(), sp.getProcedure().getSctid(), sp.getProcedure().getPt()), sp.getCategory()))
				.collect(Collectors.toList());
		log.debug("Output result -> {} ",  result);
		return result;
	}

	private ServiceRequestBo mapToServiceRequestBo(CompleteReferenceStudyDto study, BasicPatientDto patientDto) {
		return ServiceRequestBo.builder()
				.patientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
				.categoryId(study.getCategoryId())
				.institutionId(study.getInstitutionId())
				.doctorId(study.getDoctorId())
				.diagnosticReports(Arrays.asList(mapToDiagnosticReportBo(study)))
				.requestDate(LocalDateTime.now())
				.associatedSourceTypeId(study.getSourceTypeId().shortValue())
				.associatedSourceId(study.getEncounterId())
				.medicalCoverageId(study.getPatientMedicalCoverageId())
				.build();
	}

	public DiagnosticReportBo mapToDiagnosticReportBo(CompleteReferenceStudyDto studyDto) {
		DiagnosticReportBo result = new DiagnosticReportBo();
		result.setSnomed(new SnomedBo(studyDto.getPractice().getSctid(), studyDto.getPractice().getPt()));
		result.setHealthConditionId(studyDto.getHealthConditionId());
		return result;
	}

	@Override
	public void cancelServiceRequest(Integer serviceRequestId){
		log.debug("Input parameters -> serviceRequestId{}", serviceRequestId);
		serviceRequestStorage.cancelServiceRequest(serviceRequestId);
	}

}
