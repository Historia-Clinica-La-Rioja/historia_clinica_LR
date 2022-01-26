package net.pladema.authorization.application.port.exceptions;

public class InvalidUserException extends Exception {
	public final InvalidUserExceptionEnum code;
	public InvalidUserException(InvalidUserExceptionEnum code, Integer userId) {
		super(String.format("Invalid User id=%s", userId));
		this.code = code;
	}
}
