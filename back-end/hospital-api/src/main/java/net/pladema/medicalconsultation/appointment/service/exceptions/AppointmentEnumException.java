package net.pladema.medicalconsultation.appointment.service.exceptions;

import lombok.Getter;

@Getter
public enum AppointmentEnumException {
	APPOINTMENT_ID_NOT_FOUND,
	TRANSITION_STATE_INVALID,
	ABSENT_REASON_REQUIRED,
	DIARY_PROFESSIONAL_INVALID
}
