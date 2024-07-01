package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;


import java.time.LocalDate;
import java.util.Collection;

import ar.lamansys.sgh.publicapi.appointment.application.cancelAppointment.CancelAppointment;
import ar.lamansys.sgh.publicapi.appointment.application.fetchappointmentsbyinstitution.FetchAppointmentsByInstitution;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@RequestMapping("/public-api/institution/{institutionId}/appointment")
@Tag(name = "PublicApi Turnos", description = "Appointment by institution")
@RestController
public class AppointmentByInstitutionPublicController {
	private final LocalDateMapper localDateMapper;
	private final FetchAppointmentsByInstitution fetchAppointmentsByInstitution;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public Collection<PublicAppointmentListDto> getList(
			@PathVariable(name = "institutionId") Integer institutionId,
			@RequestParam(name = "identificationNumber", required = false) String identificationNumber,
			@RequestParam(name = "startDate", required = false) String startDateStr,
			@RequestParam(name = "endDate", required = false) String endDateStr
	) {
		LocalDate startDate = localDateMapper.fromStringToLocalDate(startDateStr);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(endDateStr);
		return fetchAppointmentsByInstitution.run(institutionId, identificationNumber, startDate, endDate);
	}

}
