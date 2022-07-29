package net.pladema.medicalconsultation.appointment.service.exceptions;

import lombok.Getter;

@Getter
public class AppointmentNotFoundException extends RuntimeException {

	private AppointmentNotFoundEnumException code;

	public AppointmentNotFoundException(AppointmentNotFoundEnumException code, String errorMessage){
		super(errorMessage);
		this.code = code;
	}

	public String getCode() {
		return code.name();
	}
}
