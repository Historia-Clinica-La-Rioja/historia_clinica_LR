package net.pladema.user.controller.exceptions;

import lombok.Getter;

@Getter
public class BackofficeUserException extends RuntimeException{

	private final BackofficeUserExceptionEnum code;
	public BackofficeUserException(BackofficeUserExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}
