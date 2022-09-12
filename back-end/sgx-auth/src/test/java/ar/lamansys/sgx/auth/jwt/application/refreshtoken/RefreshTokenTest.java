package ar.lamansys.sgx.auth.jwt.application.refreshtoken;

import ar.lamansys.sgx.auth.jwt.application.generatetoken.GenerateToken;
import ar.lamansys.sgx.auth.jwt.application.refreshtoken.exceptions.BadRefreshTokenException;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoStorage;
import ar.lamansys.sgx.auth.oauth.application.RefreshOAuthToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class RefreshTokenTest {

    private RefreshToken refreshToken;

    @Mock
    private UserInfoStorage userInfoStorage;

    @Mock
    private RefreshOAuthToken refreshOAuthToken;

    @Mock
    private GenerateToken generateToken;

    @BeforeEach
    void setUp(){
        refreshToken = new RefreshTokenImpl("test_secret", userInfoStorage, generateToken, refreshOAuthToken);
    }

    @Test
    @DisplayName("Refresh token success")
    @Disabled
    void refreshSuccess() throws BadRefreshTokenException {
        when(userInfoStorage.getUser("USERNAME")).thenReturn(new UserInfoBo(1,"USERNAME", true, "PASSWORD_ENCRIPTED" ));
        when(generateToken.generateTokens(any(), any())).thenReturn(new JWTokenBo("TOKEN", "REFRESH_TOKEN"));
/*
        try (MockedStatic<JWTUtils> utilities = Mockito.mockStatic(JWTUtils.class)) {
            utilities.when(() -> JWTUtils.parseToken(any(), any(), any()))
                    .thenReturn(Optional.of(new TokenData(ETokenType.REFRESH, "USERNAME",1)));

            var result = refreshToken.execute("REFRESHTOKEN");
            assertEquals(result.token, "TOKEN");
            assertEquals(result.refreshToken, "REFRESH_TOKEN");
        }*/
    }
}