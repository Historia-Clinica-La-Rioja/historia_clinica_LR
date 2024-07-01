package ar.lamansys.sgh.publicapi.appointment.application.cancelBooking.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class CancelBookingAccessDeniedException extends PublicApiAccessDeniedException {

	public CancelBookingAccessDeniedException() {
		super("Appointment", "CancelBooking");
	}

}
