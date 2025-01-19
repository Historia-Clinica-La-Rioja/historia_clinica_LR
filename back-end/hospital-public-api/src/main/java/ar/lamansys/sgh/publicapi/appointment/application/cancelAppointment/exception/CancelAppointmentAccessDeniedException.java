package ar.lamansys.sgh.publicapi.appointment.application.cancelAppointment.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class CancelAppointmentAccessDeniedException extends PublicApiAccessDeniedException {

	public CancelAppointmentAccessDeniedException() {
		super("Appointment","CancelAppointments");
	}
}
