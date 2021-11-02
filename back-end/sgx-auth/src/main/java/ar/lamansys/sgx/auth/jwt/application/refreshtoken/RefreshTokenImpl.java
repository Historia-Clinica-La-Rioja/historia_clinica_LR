package ar.lamansys.sgx.auth.jwt.application.refreshtoken;

import ar.lamansys.sgx.auth.jwt.application.generatetoken.GenerateToken;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTUtils;
import ar.lamansys.sgx.auth.jwt.application.refreshtoken.exceptions.BadRefreshTokenException;
import ar.lamansys.sgx.auth.jwt.domain.token.ETokenType;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class RefreshTokenImpl implements RefreshToken {

    private final UserInfoStorage userInfoStorage;

    private final GenerateToken generateToken;

    private final String secret;

    public RefreshTokenImpl(@Value("${token.secret}") String secret,
                            UserInfoStorage userInfoStorage,
                            GenerateToken generateToken) {
        this.userInfoStorage = userInfoStorage;
        this.generateToken = generateToken;
        this.secret = secret;
    }

    @Override
    public JWTokenBo execute(String refreshToken) throws BadRefreshTokenException {
        return JWTUtils.parseToken(refreshToken, secret, ETokenType.REFRESH)
                .map(tokenData -> userInfoStorage.getUser(tokenData.username))
                .map(user -> generateToken.generateTokens(user.getId(), user.getUsername()))
                .orElseThrow(BadRefreshTokenException::new);
    }
}
