package net.pladema.snowstorm.services.exceptions;

public class SisaTimeoutException extends RuntimeException {

	private SisaEnumException code;

	public SisaTimeoutException(SisaEnumException code, String message) {
		super(message);
		this.code = code;
	}
}

