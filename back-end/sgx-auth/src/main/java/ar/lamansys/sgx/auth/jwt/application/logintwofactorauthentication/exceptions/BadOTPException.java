package ar.lamansys.sgx.auth.jwt.application.logintwofactorauthentication.exceptions;

import lombok.Getter;

@Getter
public class BadOTPException extends RuntimeException {

	private final BadOTPExceptionEnum code;

	public BadOTPException(BadOTPExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
