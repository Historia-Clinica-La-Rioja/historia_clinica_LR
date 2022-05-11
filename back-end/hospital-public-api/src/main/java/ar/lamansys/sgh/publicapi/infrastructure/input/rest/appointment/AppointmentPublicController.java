package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public-api/institution/{sisaCode}/appointment")
public class AppointmentPublicController {

	private final SharedBookingPort bookAppointmentPort;

	public AppointmentPublicController(SharedBookingPort bookAppointmentPort) {
		this.bookAppointmentPort = bookAppointmentPort;
	}

	@PostMapping("/booking")
	@ResponseStatus(HttpStatus.CREATED)
	public String bookPreappointment(@RequestBody BookingDto bookingDto) {
		return bookAppointmentPort.makeBooking(bookingDto);
	}


	@PutMapping("/booking/cancel")
	public void cancelBooking(@RequestBody String uuid) {
		bookAppointmentPort.cancelBooking(uuid);
		log.debug("cancel booking {}", uuid);
	}

}


