package ar.lamansys.sgh.publicapi.appointment.application.fetchbookingbyinstitution.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class BookingByInstitutionAccessDeniedException extends PublicApiAccessDeniedException {

	public BookingByInstitutionAccessDeniedException() {
		super("Appointment", "FetchBookingByInstitution");
	}
}
