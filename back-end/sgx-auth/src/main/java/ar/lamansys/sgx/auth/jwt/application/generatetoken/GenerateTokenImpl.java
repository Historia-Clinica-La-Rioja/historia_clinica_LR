package ar.lamansys.sgx.auth.jwt.application.generatetoken;

import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.shared.token.JWTUtils;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
public class GenerateTokenImpl implements GenerateToken {

    private final String secret;

    private final Duration tokenExpiration;

    private final Duration refreshTokenExpiration;

    public GenerateTokenImpl(
            @Value("${token.secret}") String secret,
            @Value("${token.expiration}") Duration tokenExpiration,
            @Value("${refreshToken.expiration}") Duration refreshTokenExpiration) {
        this.secret = secret;
        this.tokenExpiration = tokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    @Override
    public JWTokenBo generateTokens(Integer userId, String username) {
        String token = createNormalToken(userId, username);
        String refreshToken = createRefreshToken(username);
        return new JWTokenBo(token, refreshToken);
    }

    private String createRefreshToken(String username) {
        Map<String, Object> claims = Map.of(
                JWTUtils.TOKEN_CLAIM_TYPE, ETokenType.REFRESH
        );
        return JWTUtils.generate(claims, username, secret, refreshTokenExpiration);
    }

    private String createNormalToken(Integer userId, String username) {
        Map<String, Object> claims = Map.of(
                "userId", userId,
                JWTUtils.TOKEN_CLAIM_TYPE, ETokenType.NORMAL
        );
        return JWTUtils.generate(claims, username, secret, tokenExpiration);
    }
}
