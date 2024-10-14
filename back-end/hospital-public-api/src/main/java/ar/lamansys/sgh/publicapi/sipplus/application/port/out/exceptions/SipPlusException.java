package ar.lamansys.sgh.publicapi.sipplus.application.port.out.exceptions;

import lombok.Getter;

@Getter
public class SipPlusException extends RuntimeException {

	private final SipPlusExceptionEnum code;

	public SipPlusException(SipPlusExceptionEnum code, String errorMessage) {
		super(errorMessage);
		this.code = code;
	}

	public String getCode() {
		return this.code.name();
	}
}
