package ar.lamansys.sgh.publicapi.appointment.application.fetchallbookinginstitutionsextended.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchAllBookingInstitutionsExtendedAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchAllBookingInstitutionsExtendedAccessDeniedException() {
		super("Appointment","FetchAllBookingInstitutionsExtended");
	}
}
