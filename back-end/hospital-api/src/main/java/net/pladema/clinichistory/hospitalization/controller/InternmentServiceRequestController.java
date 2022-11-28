package net.pladema.clinichistory.hospitalization.controller;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.domain.InternmentServiceRequestBo;
import net.pladema.clinichistory.hospitalization.service.servicerequest.CreateInternmentServiceRequestService;
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
@RequestMapping("/institutions/{institutionId}/internment-service-request")
@Tag(name = "Internment Service Request", description = "Internment Service Request")
@Validated
@Slf4j
@RequiredArgsConstructor
public class InternmentServiceRequestController {

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
	private final CreateInternmentServiceRequestService createInternmentServiceRequestService;
	private final PatientExternalService patientExternalService;
	private final HospitalApiPublisher hospitalApiPublisher;
	private final StudyMapper studyMapper;

	@PostMapping("/patient/{patientId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	@Transactional
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
	public List<Integer> create(@PathVariable(name = "institutionId") Integer institutionId,
								@PathVariable(name = "patientId") Integer patientId,
								@RequestBody @Valid PrescriptionDto serviceRequestListDto
	) {
		log.debug("Input parameters -> institutionId {} patientId {}, ServiceRequestListDto {}", institutionId, patientId, serviceRequestListDto);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		Map<String, List<PrescriptionItemDto>> srGroupBy = serviceRequestListDto.getItems().stream()
				.collect(Collectors.groupingBy(PrescriptionItemDto::getCategoryId));

		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);

		ArrayList<Integer> result = new ArrayList<>();

		srGroupBy.forEach((categoryId, studyListDto) -> {
			InternmentServiceRequestBo serviceRequestBo = parseTo(
					studyMapper,
					doctorId,
					patientDto,
					categoryId,
					serviceRequestListDto.getMedicalCoverageId(),
					studyListDto);
			serviceRequestBo.setInstitutionId(institutionId);
			Integer srId = createInternmentServiceRequestService.execute(serviceRequestBo);
			hospitalApiPublisher.publish(serviceRequestBo.getPatientId(), institutionId, getTopicToPublish(categoryId) );
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

	public InternmentServiceRequestBo parseTo(StudyMapper studyMapper, Integer doctorId, BasicPatientDto patientDto, String categoryId, Integer medicalCoverageId, List<PrescriptionItemDto> studies){
		log.debug("parseTo -> doctorId {}, patientDto {}, medicalCoverageId {}, studies {} ", doctorId, patientDto, medicalCoverageId, studies);
		InternmentServiceRequestBo result = new InternmentServiceRequestBo();
		result.setCategoryId(categoryId);
		result.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));
		result.setDoctorId(doctorId);
		result.setDiagnosticReports(studyMapper.parseToList(studies));
		log.debug("Output -> {}", result);
		return result;
	}
}
