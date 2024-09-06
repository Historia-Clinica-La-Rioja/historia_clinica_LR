package net.pladema.emergencycare.servicerequest.controller;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.service.domain.ExtendedServiceRequestBo;

import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyMapper;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.emergencycare.servicerequest.service.CreateEmergencyCareServiceRequestService;
import net.pladema.events.EHospitalApiTopicDto;

import net.pladema.events.HospitalApiPublisher;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

import org.apache.tomcat.jni.Local;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/institutions/{institutionId}/emergency-care/episode/{episodeId}/emergency-care-service-request")
@Tag(name = "Emergency Care Service Request", description = "Emergency Care Service Request")
@Validated
@Slf4j
@RequiredArgsConstructor
public class EmergencyCareServiceRequestController {

	private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

	private final PatientExternalService patientExternalService;

	private final HospitalApiPublisher hospitalApiPublisher;

	private final CreateEmergencyCareServiceRequestService emergencyCareServiceRequestService;

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;

	private final StudyMapper studyMapper;

	private final LocalDateMapper localDateMapper;

	@PostMapping("/patient/{patientId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	@Transactional
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PROFESIONAL_DE_SALUD')")
	public List<Integer> create(@PathVariable(name = "institutionId") Integer institutionId,
								@PathVariable(name = "episodeId") Integer episodeId,
								@PathVariable(name = "patientId") Integer patientId,
								@RequestBody @Valid PrescriptionDto serviceRequestListDto) {
		log.debug("Input parameters -> institutionId {}, episodeId {}, patientId {}, ServiceRequestListDto {}", institutionId, episodeId, patientId, serviceRequestListDto);
		Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
		Map<String, List<PrescriptionItemDto>> srGroupBy = serviceRequestListDto.getItems().stream().collect(Collectors.groupingBy(PrescriptionItemDto::getCategoryId));
		Integer patientMedicalCoverageId = emergencyCareEpisodeService.getPatientMedicalCoverageIdByEpisode(episodeId);
		BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);
		List<Integer> result = new ArrayList<>();
		srGroupBy.forEach((categoryId, studyListDto) ->	handleStudyList(institutionId, episodeId, doctorId, patientMedicalCoverageId, patientDto, result, categoryId, studyListDto, serviceRequestListDto.getObservations(), serviceRequestListDto.getStudyType().getId(), serviceRequestListDto.getRequiresTransfer(), serviceRequestListDto.getDeferredDate()));
		log.debug("Output -> {}", result);
		return result;
	}

	private void handleStudyList(Integer institutionId, Integer episodeId, Integer doctorId, Integer medicalCoverageId, BasicPatientDto patientDto, List<Integer> result, String categoryId, List<PrescriptionItemDto> studyListDto, String observations, Short studyTypeId, Boolean requiresTransfer, DateTimeDto deferredDate) {
		ExtendedServiceRequestBo serviceRequestBo = parseTo(doctorId, patientDto, categoryId, medicalCoverageId, studyListDto, observations, studyTypeId, requiresTransfer, deferredDate);
		serviceRequestBo.setInstitutionId(institutionId);
		Integer srId = emergencyCareServiceRequestService.execute(serviceRequestBo, episodeId);
		hospitalApiPublisher.publish(serviceRequestBo.getPatientId(), institutionId, getTopicToPublish(categoryId));
		result.add(srId);
	}

	private EHospitalApiTopicDto getTopicToPublish (String categoryId) {
		if (categoryId.equals("108252007"))
			return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST__LABORATORY;
		if (categoryId.equals("363679005"))
			return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST__IMAGE;
		return EHospitalApiTopicDto.CLINIC_HISTORY__HOSPITALIZATION__SERVICE_RESQUEST;
	}

	public ExtendedServiceRequestBo parseTo(Integer doctorId, BasicPatientDto patientDto, String categoryId, Integer medicalCoverageId, List<PrescriptionItemDto> studies, String observations, Short studyTypeId, Boolean requiresTransfer, DateTimeDto deferredDate){
		log.debug("parseTo -> doctorId {}, patientDto {}, categoryId {}, medicalCoverageId {}, studies {} ", doctorId, patientDto, categoryId, medicalCoverageId, studies);
		ExtendedServiceRequestBo result = new ExtendedServiceRequestBo();
		result.setCategoryId(categoryId);
		result.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));
		result.setDoctorId(doctorId);
		result.setDiagnosticReports(studyMapper.parseToList(studies));
		result.setMedicalCoverageId(medicalCoverageId);
		result.setObservations(observations);
		result.setStudyTypeId(studyTypeId);
		result.setRequiresTransfer(requiresTransfer);
		result.setDeferredDate(localDateMapper.fromDateDto(deferredDate.getDate()).atTime(localDateMapper.fromTimeDto(deferredDate.getTime())) );
		log.debug("Output -> {}", result);
		return result;
	}

}