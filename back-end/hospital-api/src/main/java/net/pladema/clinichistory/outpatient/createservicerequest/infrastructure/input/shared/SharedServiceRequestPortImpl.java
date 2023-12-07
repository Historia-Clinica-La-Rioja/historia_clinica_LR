package net.pladema.clinichistory.outpatient.createservicerequest.infrastructure.input.shared;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedServiceRequestPort;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceStudyDto;

import lombok.RequiredArgsConstructor;

import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.patient.controller.service.PatientExternalService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class SharedServiceRequestPortImpl implements SharedServiceRequestPort {

	private final PatientExternalService patientExternalService;

	private final CreateServiceRequestService createServiceRequestService;

	@Override
	public Integer create(CompleteReferenceStudyDto study) {
		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(study.getPatientId());
		ServiceRequestBo serviceRequest = mapToServiceRequestBo(study, patientDto);
		return createServiceRequestService.execute(serviceRequest);
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

}
