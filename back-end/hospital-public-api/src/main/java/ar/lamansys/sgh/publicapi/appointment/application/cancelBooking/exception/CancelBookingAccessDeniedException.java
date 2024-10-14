package ar.lamansys.sgh.publicapi.appointment.application.cancelBooking.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class CancelBookingAccessDeniedException extends PublicApiAccessDeniedException {

	public CancelBookingAccessDeniedException() {
		super("Appointment", "CancelBooking");
	}

}
