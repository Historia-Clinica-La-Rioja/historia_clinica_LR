package net.pladema.patient.controller.service.exception;

import lombok.Getter;

@Getter
public class AuditPatientException extends RuntimeException {

	private final AuditPatientExceptionEnum code;

	public AuditPatientException(AuditPatientExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}
