package ar.lamansys.sgx.auth.jwt.application.logintwofactorauthentication;

import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;

public interface LoginTwoFactorAuthentication {

	JWTokenBo execute(String code);

}
