package ar.lamansys.sgx.auth.user.application.updateownpassword.exceptions;

public class PasswordException extends RuntimeException {
	public final PasswordExceptionEnum code;
	public PasswordException(PasswordExceptionEnum code, String msg) {
		super(msg);
		this.code = code;
	}
}
