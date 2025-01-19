package ar.lamansys.sgh.publicapi.appointment.application.fetchappointmentsbyinstitution.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class AppointmentAccessDeniedException extends PublicApiAccessDeniedException {

	public AppointmentAccessDeniedException() {
		super("Appointment", "FetchAppointmentsByInstitution");
	}

}
