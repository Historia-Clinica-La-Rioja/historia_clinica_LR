package ar.lamansys.sgx.auth.twoFactorAuthentication.application.confirmTwoFactorAuthentication;

public interface ConfirmTwoFactorAuthentication {

	Boolean run(String code);

}
