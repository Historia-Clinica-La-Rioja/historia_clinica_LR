package net.pladema.sisa.refeps.services.exceptions;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class RefepsApiException extends Exception {

	private RefepsExceptionsEnum code;

	private HttpStatus statusCode;

	public RefepsApiException(RefepsExceptionsEnum refepsExceptionsEnum, HttpStatus statusCode, String message) {
		super(message);
		this.statusCode = statusCode;
		this.code = refepsExceptionsEnum;
	}

}
