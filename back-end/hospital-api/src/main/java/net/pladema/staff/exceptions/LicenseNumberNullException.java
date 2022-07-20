package net.pladema.staff.exceptions;

import lombok.Getter;

@Getter
public class LicenseNumberNullException extends RuntimeException{

	private final LicenseNumberNullExceptionEnum code;
	public LicenseNumberNullException(LicenseNumberNullExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
