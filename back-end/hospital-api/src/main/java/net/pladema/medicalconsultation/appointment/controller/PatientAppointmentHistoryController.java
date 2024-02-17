package net.pladema.medicalconsultation.appointment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.controller.dto.PatientAppointmentHistoryDto;
import net.pladema.medicalconsultation.appointment.controller.mapper.AppointmentMapper;
import net.pladema.medicalconsultation.appointment.service.GetPatientAppointmentHistoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "Patient Appointment History", description = "Patient Appointment History")
@RequestMapping("/institution/{institutionId}/appointment-history/patient/{patientId}")
@RestController
public class PatientAppointmentHistoryController {

	private final AppointmentMapper appointmentMapper;

	private final GetPatientAppointmentHistoryService getPatientAppointmentHistoryService;

	@GetMapping("/by-professional-diaries")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRATIVO')")
	public Page<PatientAppointmentHistoryDto> getPatientHistoryByProfessionalDiaries(@PathVariable(name = "institutionId") Integer institutionId,
																					 @PathVariable(name = "patientId") Integer patientId,
																					 @PageableDefault(size = 5) @SortDefault.SortDefaults({
																							 @SortDefault(sort = "dateTypeId", direction = Sort.Direction.DESC),
																							 @SortDefault(sort = "hour", direction = Sort.Direction.DESC)
																					 }) Pageable pageable) {
		log.debug("Input parameters -> institutionId {},patientId {}, pageable {}", institutionId, patientId, pageable);
		Page<PatientAppointmentHistoryDto> result = getPatientAppointmentHistoryService.run(patientId, pageable).map(appointmentMapper::toPatientAppointmentHistoryDto);
		log.debug("Output -> {}", result);
		return result;
	}

}
