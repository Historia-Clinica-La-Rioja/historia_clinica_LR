package ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyspeciality.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class AppointmentAvailabilityAccessDeniedException extends PublicApiAccessDeniedException {

	public AppointmentAvailabilityAccessDeniedException() {
		super("Appointment Availability", "FetchAvailabilityBySpecialty");
	}
}
