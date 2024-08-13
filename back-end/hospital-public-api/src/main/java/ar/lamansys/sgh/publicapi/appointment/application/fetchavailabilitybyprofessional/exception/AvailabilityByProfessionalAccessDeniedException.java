package ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessional.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class AvailabilityByProfessionalAccessDeniedException extends PublicApiAccessDeniedException {

	public AvailabilityByProfessionalAccessDeniedException() {
		super("Appointment Availability","FetchAvailabilityByProfessional");
	}
}
