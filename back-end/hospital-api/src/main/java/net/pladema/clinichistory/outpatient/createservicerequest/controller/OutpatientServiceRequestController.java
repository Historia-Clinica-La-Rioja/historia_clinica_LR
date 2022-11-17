package net.pladema.clinichistory.outpatient.createservicerequest.controller;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.outpatient.createservicerequest.service.CreateOutpatientServiceRequestService;
import net.pladema.clinichistory.outpatient.createservicerequest.service.domain.OutpatientServiceRequestBo;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyMapper;
import net.pladema.events.EHospitalApiTopicDto;
import net.pladema.events.HospitalApiPublisher;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/outpatient/service-request")
@Tag(name = "Outpatient Service Request", description = "Outpatient Service Request")
@Validated
@Slf4j
@RequiredArgsConstructor
public class OutpatientServiceRequestController {

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
	private final CreateOutpatientServiceRequestService createOutpatientServiceRequestService;
	private final PatientExternalService patientExternalService;
	private final HospitalApiPublisher hospitalApiPublisher;
	private final StudyMapper studyMapper;

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@Transactional // Transaccion compleja
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public List<Integer> create(@PathVariable(name = "institutionId") Integer institutionId, @PathVariable(name = "patientId") Integer patientId, @RequestBody @Valid PrescriptionDto serviceRequestListDto) {
		log.debug("Input parameters -> institutionId {} patientId {}, ServiceRequestListDto {}", institutionId, patientId, serviceRequestListDto);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		Map<String, List<PrescriptionItemDto>> srGroupBy = serviceRequestListDto.getItems().stream().collect(Collectors.groupingBy(PrescriptionItemDto::getCategoryId));

		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);

		ArrayList<Integer> result = new ArrayList<>();

		srGroupBy.forEach((categoryId, studyListDto) -> {
			OutpatientServiceRequestBo serviceRequestBo = this.parseTo(studyMapper, doctorId, patientDto, categoryId, serviceRequestListDto.getMedicalCoverageId(), studyListDto);
			serviceRequestBo.setInstitutionId(institutionId);
			Integer srId = createOutpatientServiceRequestService.execute(serviceRequestBo);
			hospitalApiPublisher.publish(serviceRequestBo.getPatientId(), institutionId, getTopicToPublish(categoryId));
			result.add(srId);
		});

		log.debug("Output -> {}", result);
		return result;
	}

	private EHospitalApiTopicDto getTopicToPublish (String categoryId) {
		if (categoryId.equals("108252007"))
			return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST__LABORATORY;
		if (categoryId.equals("363679005"))
			return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST__IMAGE;
		return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST;
	}

	private OutpatientServiceRequestBo parseTo(StudyMapper studyMapper, Integer doctorId, BasicPatientDto patientDto, String categoryId, Integer medicalCoverageId, List<PrescriptionItemDto> studies){
		log.debug("parseTo -> doctorId {}, patientDto {}, medicalCoverageId {}, studies {} ", doctorId, patientDto, medicalCoverageId, studies);
		OutpatientServiceRequestBo result = new OutpatientServiceRequestBo();
		result.setCategoryId(categoryId);
		result.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));
		result.setDoctorId(doctorId);
		result.setMedicalCoverageId(medicalCoverageId);
		result.setDiagnosticReports(studyMapper.parseToList(studies));
		log.debug("Output -> {}", result);
		return result;
	}

}
