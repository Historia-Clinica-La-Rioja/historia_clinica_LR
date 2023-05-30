package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.requests.servicerequests.application.CloseDraftStudyAppointmentReport;
import net.pladema.clinichistory.requests.servicerequests.application.CreateDraftStudyAppointmentReport;
import net.pladema.clinichistory.requests.servicerequests.application.GetStudyAppointment;
import net.pladema.clinichistory.requests.servicerequests.application.UpdateDraftStudyAppointmentReport;
import net.pladema.clinichistory.requests.servicerequests.domain.InformerObservationBo;
import net.pladema.clinichistory.requests.servicerequests.domain.StudyAppointmentBo;

import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto.InformerObservationDto;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.dto.StudyAppointmentDto;
import net.pladema.clinichistory.requests.servicerequests.infrastructure.input.rest.mapper.StudyAppointmentMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	private final CreateDraftStudyAppointmentReport createDraftStudyAppointmentReport;
	private final UpdateDraftStudyAppointmentReport updateDraftStudyAppointmentReport;
	private final CloseDraftStudyAppointmentReport closeDraftStudyAppointmentReport;

	@GetMapping(value = "/study/by-appointment")
	public ResponseEntity<StudyAppointmentDto> getStudyByAppointment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId
	) {
		log.debug("Get study by appointmentId {}, institutionId {}", institutionId, appointmentId);
		StudyAppointmentBo result = getStudyAppointment.run(appointmentId);
		return ResponseEntity.ok().body(studyAppointmentMapper.toStudyAppointmentDto(result));
	}
	
	@PostMapping(value = "/createDraftReport/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'INFORMADOR')")
	public ResponseEntity<Long> createDraftReport(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestBody InformerObservationDto informerObservationDto
	) {
		log.debug("Input parameters to create draft report for study appointmentId {}, institutionId {}, informerObservationDto {}", institutionId, appointmentId, informerObservationDto);
		InformerObservationBo informerObservationBo = studyAppointmentMapper.toInformerObservationBo(informerObservationDto);
		informerObservationBo.setInstitutionId(institutionId);
		Long result = createDraftStudyAppointmentReport.execute(appointmentId, informerObservationBo);
		log.debug("Output", result);
		return ResponseEntity.ok().body(result);
	}

	@PutMapping(value = "/updateDraftReport/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'INFORMADOR')")
	public ResponseEntity<Long> updateDraftReport(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestBody InformerObservationDto informerObservationDto
	) {
		log.debug("Input parameters to update draft report for study appointmentId {}, institutionId {}, informerObservationDto {}", institutionId, appointmentId, informerObservationDto);
		InformerObservationBo informerObservationBo = studyAppointmentMapper.toInformerObservationBo(informerObservationDto);
		informerObservationBo.setInstitutionId(institutionId);
		Long result = updateDraftStudyAppointmentReport.execute(appointmentId, informerObservationBo);
		log.debug("Output -> {}", result);
		return ResponseEntity.ok().body(result);
	}

	@PutMapping(value = "/closeDraftReport/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'INFORMADOR')")
	public ResponseEntity<Long> closeDraftReport(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestBody InformerObservationDto informerObservationDto
	) {
		log.debug("Input parameters to close draft report for study appointmentId {}, institutionId {}, informerObservationDto {}", institutionId, appointmentId, informerObservationDto);
		InformerObservationBo informerObservationBo = studyAppointmentMapper.toInformerObservationBo(informerObservationDto);
		informerObservationBo.setInstitutionId(institutionId);
		Long result = closeDraftStudyAppointmentReport.execute(appointmentId, informerObservationBo);
		log.debug("Output", result);
		return ResponseEntity.ok().body(result);
	}

}
