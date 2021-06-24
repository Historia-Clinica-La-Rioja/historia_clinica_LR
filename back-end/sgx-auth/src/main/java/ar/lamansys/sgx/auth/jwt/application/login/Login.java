package ar.lamansys.sgx.auth.jwt.application.login;

import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.domain.LoginBo;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;

public interface Login {

    JWTokenBo execute(LoginBo login) throws BadLoginException;
}
