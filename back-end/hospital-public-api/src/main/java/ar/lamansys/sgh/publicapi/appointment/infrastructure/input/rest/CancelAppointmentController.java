package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.appointment.application.cancelAppointment.CancelAppointment;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RequestMapping("/public-api/institution/{institutionId}/appointment/{appointmentId}/cancel")
@Tag(name = "PublicApi Turnos", description = "Cancel appointment by institution")
@RestController
public class CancelAppointmentController {
	private final CancelAppointment cancelAppointment;

	@PutMapping()
	public ResponseEntity<String> cancelAppointment(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId,
			@RequestParam(name = "reason") String reason) {
		cancelAppointment.run(institutionId, appointmentId, reason);
		return ResponseEntity.ok().body(String.format("El turno con id %s fue cancelado con éxito", appointmentId));
	}

}
