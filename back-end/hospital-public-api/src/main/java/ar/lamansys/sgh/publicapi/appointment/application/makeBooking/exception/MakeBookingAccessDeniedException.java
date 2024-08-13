package ar.lamansys.sgh.publicapi.appointment.application.makeBooking.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class MakeBookingAccessDeniedException extends PublicApiAccessDeniedException {

	public MakeBookingAccessDeniedException() {
		super("Appointment","MakeBoking");
	}
}
