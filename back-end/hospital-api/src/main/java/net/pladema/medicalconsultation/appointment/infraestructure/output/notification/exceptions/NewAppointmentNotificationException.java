package net.pladema.medicalconsultation.appointment.infraestructure.output.notification.exceptions;

import lombok.Getter;

@Getter
public class NewAppointmentNotificationException extends RuntimeException {

	private final NewAppointmentNotificationEnumException code;

	public NewAppointmentNotificationException(NewAppointmentNotificationEnumException code, String errorMessage) {
		super(errorMessage);
		this.code = code;
	}
}
