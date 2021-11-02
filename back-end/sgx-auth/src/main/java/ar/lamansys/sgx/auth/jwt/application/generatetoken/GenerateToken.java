package ar.lamansys.sgx.auth.jwt.application.generatetoken;

import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;

public interface GenerateToken {

    JWTokenBo generateTokens(Integer userId, String username);
}

