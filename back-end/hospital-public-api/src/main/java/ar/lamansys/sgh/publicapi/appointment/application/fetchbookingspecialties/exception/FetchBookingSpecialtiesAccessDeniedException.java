package ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialties.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchBookingSpecialtiesAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchBookingSpecialtiesAccessDeniedException() {
		super("Appointment", "FetchBookingSpecialties");
	}
}
