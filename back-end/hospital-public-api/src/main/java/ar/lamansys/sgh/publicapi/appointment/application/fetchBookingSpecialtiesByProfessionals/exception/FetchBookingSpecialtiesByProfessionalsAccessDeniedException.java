package ar.lamansys.sgh.publicapi.appointment.application.fetchBookingSpecialtiesByProfessionals.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchBookingSpecialtiesByProfessionalsAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchBookingSpecialtiesByProfessionalsAccessDeniedException() {
		super("Appointment", "FetchBookingSpecialtiesByProfessionals");
	}
}
