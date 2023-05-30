package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.servicerequests.application.GetStudyAppointment;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyAppointmentBo;

import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto.StudyAppointmentDto;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.mapper.StudyAppointmentMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/studyAppointmentReport")
@Tag(name = "Study Appointment Report", description = "Study Appointment Report")
public class StudyAppointmentReportController {

	private final GetStudyAppointment getStudyAppointment;
	private final StudyAppointmentMapper studyAppointmentMapper;
	
	@GetMapping(value = "/study/by-appointment")
	public ResponseEntity<StudyAppointmentDto> getStudyByAppointment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId
	) {
		log.debug("Get study by appointmentId {}, institutionId {}", institutionId, appointmentId);
		StudyAppointmentBo result = getStudyAppointment.run(appointmentId);
		return ResponseEntity.ok().body(studyAppointmentMapper.toStudyAppointmentDto(result));
	}
}
