package net.pladema.sisa.refeps.services.exceptions;

import lombok.Getter;

@Getter
public class RefepsLicenseException extends RuntimeException {

	public final RefepsExceptionsEnum code;

	public RefepsLicenseException(RefepsExceptionsEnum code, String message) {
		super(message);
		this.code = code;
	}

}
