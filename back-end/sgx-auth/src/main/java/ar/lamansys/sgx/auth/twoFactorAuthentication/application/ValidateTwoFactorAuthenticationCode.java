package ar.lamansys.sgx.auth.twoFactorAuthentication.application;

public interface ValidateTwoFactorAuthenticationCode {

	boolean run(String code);

}
