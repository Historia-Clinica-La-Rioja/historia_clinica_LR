package net.pladema.hl7.dataexchange.exceptions;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class PrescriptionException extends RuntimeException {

	private PrescriptionExceptionEnum code;

	private HttpStatus status;

	public PrescriptionException(PrescriptionExceptionEnum code, HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.code = code;
	}

	public String getCode() {
		return code.name();
	}

}
