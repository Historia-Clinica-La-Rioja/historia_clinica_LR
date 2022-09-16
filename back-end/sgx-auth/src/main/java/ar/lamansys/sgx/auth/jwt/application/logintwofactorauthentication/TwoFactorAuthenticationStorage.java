package ar.lamansys.sgx.auth.jwt.application.logintwofactorauthentication;

public interface TwoFactorAuthenticationStorage {

	Boolean verifyCode(String code);

}
