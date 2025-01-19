package ar.lamansys.sgh.publicapi.appointment.application.fetchbookingspecialtiesbyprofessional.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchBookingSpecialtiesByProfessionalAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchBookingSpecialtiesByProfessionalAccessDeniedException() {
		super("Appointment", "FetchBookingSpecialtiesByProfessional");
	}
}
