package ar.lamansys.sgh.publicapi.appointment.application.cancelAppointment.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class CancelAppointmentAccessDeniedException extends PublicApiAccessDeniedException {

	public CancelAppointmentAccessDeniedException() {
		super("Appointment","CancelAppointments");
	}
}
