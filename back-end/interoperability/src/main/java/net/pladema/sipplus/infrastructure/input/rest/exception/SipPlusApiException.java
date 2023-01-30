package net.pladema.sipplus.infrastructure.input.rest.exception;

import lombok.Getter;

@Getter
public class SipPlusApiException extends RuntimeException {

	public final SipPlusApiExceptionEnum code;

	public SipPlusApiException(SipPlusApiExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}