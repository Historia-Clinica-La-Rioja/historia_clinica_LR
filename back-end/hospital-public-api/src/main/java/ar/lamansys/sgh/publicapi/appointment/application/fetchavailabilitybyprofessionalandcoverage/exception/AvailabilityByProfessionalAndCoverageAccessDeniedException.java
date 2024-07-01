package ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyprofessionalandcoverage.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class AvailabilityByProfessionalAndCoverageAccessDeniedException extends PublicApiAccessDeniedException {

	public AvailabilityByProfessionalAndCoverageAccessDeniedException() {
		super("Appointment Availability", "FetchAvailabilityByProfessionalAndCoverage");
	}
}
