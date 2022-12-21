package ar.lamansys.sgx.auth.user.infrastructure.output.notification.exceptions;

import lombok.Getter;

@Getter
public class RestorePasswordNotificationException extends RuntimeException {

	private final RestorePasswordNotificationExceptionEnum code;

	public RestorePasswordNotificationException(RestorePasswordNotificationExceptionEnum code, String message){
		super(message);
		this.code = code;
	}
}
