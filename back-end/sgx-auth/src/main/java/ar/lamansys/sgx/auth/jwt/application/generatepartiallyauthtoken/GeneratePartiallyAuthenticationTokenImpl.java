package ar.lamansys.sgx.auth.jwt.application.generatepartiallyauthtoken;

import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.shared.token.JWTUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class GeneratePartiallyAuthenticationTokenImpl implements GeneratePartiallyAuthenticationToken {

    private final String secret;

    private final Duration tokenExpiration;

    public GeneratePartiallyAuthenticationTokenImpl(
            @Value("${token.secret}") String secret,
            @Value("${token.expiration}") Duration tokenExpiration) {
        this.secret = secret;
        this.tokenExpiration = tokenExpiration;
    }

    @Override
    public JWTokenBo run(Integer userId, String username) {
        String token = createPartiallyAuthenticatedToken(userId, username);
        return new JWTokenBo(token, null);
    }

    private String createPartiallyAuthenticatedToken(Integer userId, String username) {
        Map<String, Object> claims = Map.of(
                "userId", userId,
                JWTUtils.TOKEN_CLAIM_TYPE, ETokenType.PARTIALLY_AUTHENTICATED
        );
        return JWTUtils.generate(claims, username, secret, tokenExpiration);
    }
}
