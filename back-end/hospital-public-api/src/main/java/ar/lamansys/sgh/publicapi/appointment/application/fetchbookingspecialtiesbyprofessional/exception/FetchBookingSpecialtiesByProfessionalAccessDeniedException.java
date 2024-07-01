package ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialtiesbyprofessional.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchBookingSpecialtiesByProfessionalAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchBookingSpecialtiesByProfessionalAccessDeniedException() {
		super("Appointment", "FetchBookingSpecialtiesByProfessional");
	}
}
