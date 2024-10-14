package ar.lamansys.sgh.publicapi.appointment.application.fetchBookingProfessionals.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchBookingProfessionalsAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchBookingProfessionalsAccessDeniedException() {
		super("Appointment", "FetchBookingProfessional");
	}
}
