package ar.lamansys.sgx.auth.jwt.application.generatepartiallyauthtoken;

import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;

public interface GeneratePartiallyAuthenticationToken {

    JWTokenBo run(Integer userId, String username);

}
