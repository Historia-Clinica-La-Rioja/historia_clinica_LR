package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment;


import java.time.LocalDate;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.SharedAppointmentPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto.PublicAppointmentListDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public-api/institution/{institutionId}/appointment")
@Tag(name = "Public Api", description = "Appointment by institution")
public class AppointmentByInstitutionPublicController {
	private final SharedAppointmentPort appointmentPort;
	private final LocalDateMapper localDateMapper;
	public AppointmentByInstitutionPublicController(SharedAppointmentPort appointmentPort,
													LocalDateMapper localDateMapper) {
		this.appointmentPort = appointmentPort;
		this.localDateMapper = localDateMapper;
	}

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
		return appointmentPort.fetchAppointments(institutionId, identificationNumber, null, startDate, endDate);
	}

	@PutMapping("/{appointmentId}/cancel")
	public void cancelAppointment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestParam(name = "reason") String reason) {
		appointmentPort.cancelAppointment(institutionId, appointmentId, reason);
	}
}
