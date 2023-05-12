package net.pladema.medicalconsultation.appointment.service.exceptions;

import lombok.Getter;

@Getter
public class AppointmentException extends RuntimeException {

	private AppointmentEnumException code;

	public AppointmentException(AppointmentEnumException code, String errorMessage){
		super(errorMessage);
		this.code = code;
	}

	public String getCode() {
		return code.name();
	}
}
