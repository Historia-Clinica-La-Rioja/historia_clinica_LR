package net.pladema.security.exceptions;

public class JWTParseException extends RuntimeException {
	public JWTParseException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
