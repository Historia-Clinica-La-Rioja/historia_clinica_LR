package net.pladema.hl7.dataexchange.exceptions;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class DispenseValidationException extends RuntimeException {

	private DispenseValidationEnumException code;

	private HttpStatus status;

	public DispenseValidationException(DispenseValidationEnumException code, HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.code = code;
	}

	public String getCode() {
		return code.name();
	}

}
