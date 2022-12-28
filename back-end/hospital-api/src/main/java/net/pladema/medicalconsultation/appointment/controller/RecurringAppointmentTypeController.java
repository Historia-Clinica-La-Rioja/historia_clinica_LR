package net.pladema.medicalconsultation.appointment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicalconsultation.appointment.application.recurringappointmenttype.FetchRecurringAppointmentType;

import net.pladema.medicalconsultation.appointment.controller.dto.RecurringTypeDto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/recurring-appointment-type")
@Tag(name = "RecurringAppointmentType", description = "RecurringAppointmentType")
public class RecurringAppointmentTypeController {

	public static final String OUTPUT = "Output -> {}";

	public final FetchRecurringAppointmentType fetchRecurringAppointmentType;

	public RecurringAppointmentTypeController(FetchRecurringAppointmentType fetchRecurringAppointmentType) {
		this.fetchRecurringAppointmentType = fetchRecurringAppointmentType;
	}

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ADMINISTRADOR_AGENDA, ENFERMERO')")
	public List<RecurringTypeDto> getRecurringAppointmentType(@PathVariable(name = "institutionId") Integer institutionId) {
		log.debug("Input parameters -> institutionId {}", institutionId);
		List<RecurringTypeDto> result = fetchRecurringAppointmentType.run()
				.stream()
				.map(bo -> new RecurringTypeDto(bo.getId(), bo.getValue()))
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}
}
