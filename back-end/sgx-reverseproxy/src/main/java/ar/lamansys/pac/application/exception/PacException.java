package ar.lamansys.pac.application.exception;

import lombok.Getter;

@Getter
public class PacException extends RuntimeException {

	private final PacExceptionEnum code;

	public PacException(PacExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
