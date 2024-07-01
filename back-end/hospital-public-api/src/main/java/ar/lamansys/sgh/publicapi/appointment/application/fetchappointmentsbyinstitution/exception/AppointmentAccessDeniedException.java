package ar.lamansys.sgh.publicapi.appointment.application.fetchappointmentsbyinstitution.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class AppointmentAccessDeniedException extends PublicApiAccessDeniedException {

	public AppointmentAccessDeniedException() {
		super("Appointment", "FetchAppointmentsByInstitution");
	}

}
