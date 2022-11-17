package ar.lamansys.sgx.auth.jwt.application.refreshtoken;

import ar.lamansys.sgx.auth.jwt.application.generatetoken.GenerateToken;
import ar.lamansys.sgx.auth.jwt.infrastructure.output.token.TokenUtils;
import ar.lamansys.sgx.auth.jwt.application.refreshtoken.exceptions.BadRefreshTokenException;
import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoStorage;
import ar.lamansys.sgx.auth.oauth.application.RefreshOAuthToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


@Service
public class RefreshTokenImpl implements RefreshToken {

    private final UserInfoStorage userInfoStorage;

    private final GenerateToken generateToken;

    private final RefreshOAuthToken refreshOAuthToken;

    private final String secret;

    @Value("${ws.oauth.enabled:false}")
    private boolean oAuthServiceEnabled;

    public RefreshTokenImpl(@Value("${token.secret}") String secret,
                            UserInfoStorage userInfoStorage,
                            GenerateToken generateToken,
                            RefreshOAuthToken refreshOAuthToken) {
        this.userInfoStorage = userInfoStorage;
        this.generateToken = generateToken;
        this.refreshOAuthToken = refreshOAuthToken;
        this.secret = secret;
    }

    @Override
    public JWTokenBo execute(String refreshToken) throws BadRefreshTokenException {
        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new BadRefreshTokenException();
        }
        if (oAuthServiceEnabled) {
            return refreshOAuthToken.run(refreshToken).orElseThrow(BadRefreshTokenException::new);
        }
        return TokenUtils.parseToken(refreshToken, secret, ETokenType.REFRESH)
                .map(tokenData -> userInfoStorage.getUser(tokenData.username))
                .map(user -> generateToken.generateTokens(user.getId(), user.getUsername()))
                .orElseThrow(BadRefreshTokenException::new);
    }
}
