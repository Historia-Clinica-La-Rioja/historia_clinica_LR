package ar.lamansys.sgx.auth.twoFactorAuthentication.application.exceptions;

import lombok.Getter;

@Getter
public class TwoFactorAuthenticationException extends RuntimeException {

	public final TwoFactorAuthenticationExceptionEnum code;

	public TwoFactorAuthenticationException(TwoFactorAuthenticationExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
