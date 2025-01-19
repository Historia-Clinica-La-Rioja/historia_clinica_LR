package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.exceptions;

import lombok.Getter;

@Getter
public class SaveExternalBookingException extends Exception {

	private static final long serialVersionUID = -560084703368463709L;

	public final SaveExternalBookingExceptionEnum code;

	public SaveExternalBookingException(SaveExternalBookingExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
