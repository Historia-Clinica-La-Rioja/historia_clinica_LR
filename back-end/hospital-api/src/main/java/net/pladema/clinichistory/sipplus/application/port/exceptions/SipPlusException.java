package net.pladema.clinichistory.sipplus.application.port.exceptions;

import lombok.Getter;

@Getter
public class SipPlusException extends RuntimeException {

	private final SipPlusExceptionEnum code;

	public SipPlusException(SipPlusExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}