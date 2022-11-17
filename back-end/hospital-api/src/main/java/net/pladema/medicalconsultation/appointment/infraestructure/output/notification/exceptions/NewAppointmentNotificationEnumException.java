package net.pladema.medicalconsultation.appointment.infraestructure.output.notification.exceptions;

import lombok.Getter;

@Getter
public enum NewAppointmentNotificationEnumException {
	PROFESSIONAL_NAME_NOT_FOUND,
	DIARY_NOT_FOUND;
}
