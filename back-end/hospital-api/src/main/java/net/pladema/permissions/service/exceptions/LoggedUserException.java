package net.pladema.permissions.service.exceptions;

public class LoggedUserException extends RuntimeException {
	public final LoggedUserExceptionEnum code;

	public LoggedUserException(LoggedUserExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}
