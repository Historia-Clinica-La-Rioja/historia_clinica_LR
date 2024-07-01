package ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutionsextended.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchAllBookingInstitutionsExtendedAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchAllBookingInstitutionsExtendedAccessDeniedException() {
		super("Appointment","FetchAllBookingInstitutionsExtended");
	}
}
