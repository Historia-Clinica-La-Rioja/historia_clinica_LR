package ar.lamansys.sgh.publicapi.appointment.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.appointment.application.cancelBooking.CancelBookingByInstitution;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@Slf4j
@RequestMapping("/public-api/institution/{institutionId}/appointment/booking/cancel")
@Tag(name = "PublicApi Turnos", description = "Booking by institution")
@RestController
public class CancelBookingController {

	private final CancelBookingByInstitution cancelBookingByInstitution;

	@PutMapping()
	public void cancelBooking(@PathVariable(name = "institutionId") Integer institutionId, @RequestBody String uuid) {
		cancelBookingByInstitution.run(institutionId, uuid);
		log.debug("cancel booking {}", uuid);
	}
}
