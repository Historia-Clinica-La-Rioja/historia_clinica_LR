package ar.lamansys.sgx.auth.oauth.application.ports;

import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;

import java.util.Optional;

public interface OAuthTokenStorage {

    Optional<JWTokenBo> refreshToken(String refreshToken);

}
