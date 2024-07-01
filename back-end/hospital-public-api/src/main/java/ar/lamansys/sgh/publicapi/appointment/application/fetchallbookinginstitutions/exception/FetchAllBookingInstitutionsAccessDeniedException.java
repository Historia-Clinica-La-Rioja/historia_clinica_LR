package ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutions.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchAllBookingInstitutionsAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchAllBookingInstitutionsAccessDeniedException() {
		super("Appointment","FetchAllBookingInstitutions");
	}
}
