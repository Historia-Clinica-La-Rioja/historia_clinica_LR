package ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessional.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class AvailabilityByProfessionalAccessDeniedException extends PublicApiAccessDeniedException {

	public AvailabilityByProfessionalAccessDeniedException() {
		super("Appointment Availability","FetchAvailabilityByProfessional");
	}
}
