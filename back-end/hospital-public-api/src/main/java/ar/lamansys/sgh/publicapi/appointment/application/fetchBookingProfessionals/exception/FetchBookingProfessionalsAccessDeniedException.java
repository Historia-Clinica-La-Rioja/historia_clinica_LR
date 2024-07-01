package ar.lamansys.sgh.publicapi.appointment.application.fetchBookingProfessionals.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchBookingProfessionalsAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchBookingProfessionalsAccessDeniedException() {
		super("Appointment", "FetchBookingProfessional");
	}
}
