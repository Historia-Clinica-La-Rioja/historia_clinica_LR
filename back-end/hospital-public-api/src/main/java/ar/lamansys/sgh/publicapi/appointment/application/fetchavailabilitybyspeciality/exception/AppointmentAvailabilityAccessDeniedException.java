package ar.lamansys.sgh.publicapi.appointment.application.fetchavailabilitybyspeciality.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class AppointmentAvailabilityAccessDeniedException extends PublicApiAccessDeniedException {

	public AppointmentAvailabilityAccessDeniedException() {
		super("Appointment Availability", "FetchAvailabilityBySpecialty");
	}
}
