package ar.lamansys.sgx.auth.twoFactorAuthentication.application.validateTwoFactorAuthentication;

public interface ValidateTwoFactorAuthenticationCode {

	boolean run(String code);

}
