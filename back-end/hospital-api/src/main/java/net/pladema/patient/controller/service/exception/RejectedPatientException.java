package net.pladema.patient.controller.service.exception;

import lombok.Getter;

@Getter
public class RejectedPatientException extends RuntimeException {

	private final RejectedPatientExceptionEnum code;

	public RejectedPatientException(RejectedPatientExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}
