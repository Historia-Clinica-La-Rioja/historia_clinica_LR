package ar.lamansys.sgx.auth.jwt.application.refreshtoken;

import ar.lamansys.sgx.auth.jwt.application.refreshtoken.exceptions.BadRefreshTokenException;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;

public interface RefreshToken {

    JWTokenBo execute(String refreshToken) throws BadRefreshTokenException;
}
