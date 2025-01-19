package ar.lamansys.sgh.publicapi.appointment.application.checkmailexists;

import ar.lamansys.sgh.shared.infrastructure.input.service.booking.SharedBookingPort;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckMailExists {
	private final SharedBookingPort bookAppointmentPort;

	public boolean run(String email) {
		return bookAppointmentPort.checkMailExists(email);
	}
}
