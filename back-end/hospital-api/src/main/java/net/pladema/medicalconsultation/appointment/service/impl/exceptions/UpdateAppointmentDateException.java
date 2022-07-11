package net.pladema.medicalconsultation.appointment.service.impl.exceptions;

import lombok.Getter;

@Getter
public class UpdateAppointmentDateException extends RuntimeException{

	private final UpdateAppointmentDateExceptionEnum code;

	public UpdateAppointmentDateException(UpdateAppointmentDateExceptionEnum code, String message){
		super(message);
		this.code = code;
	}

	public String getCode() {
		return code.name();
	}

}
