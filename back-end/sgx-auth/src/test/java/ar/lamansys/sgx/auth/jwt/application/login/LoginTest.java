package ar.lamansys.sgx.auth.jwt.application.login;

import ar.lamansys.sgx.auth.jwt.application.generatepartiallyauthtoken.GeneratePartiallyAuthenticationToken;
import ar.lamansys.sgx.auth.jwt.application.generatetoken.GenerateToken;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import ar.lamansys.sgx.auth.jwt.domain.LoginBo;
import ar.lamansys.sgx.auth.jwt.domain.token.JWTokenBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoBo;
import ar.lamansys.sgx.auth.jwt.domain.user.UserInfoStorage;
import ar.lamansys.sgx.auth.user.application.fetchuserhastwofactorauthenticationenabled.FetchUserHasTwoFactorAuthenticationEnabled;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LoginTest {

    private Login login;

    @Mock
    private UserInfoStorage userInfoStorage;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @Mock
    private GenerateToken generateToken;

    @Mock
    private FeatureFlagsService featureFlagsService;

    @Mock
    private GeneratePartiallyAuthenticationToken generatePartiallyAuthenticationToken;

    @Mock
    private FetchUserHasTwoFactorAuthenticationEnabled fetchUserHasTwoFactorAuthenticationEnabled;

    @BeforeEach
    void setUp(){
        login = new LoginJWTImpl(userInfoStorage, passwordEncryptor, generateToken,
                featureFlagsService, generatePartiallyAuthenticationToken, fetchUserHasTwoFactorAuthenticationEnabled);
    }

    @Test
    @DisplayName("Login success")
    void loginSuccess() throws BadLoginException {
        when(userInfoStorage.getUser("USERNAME")).thenReturn(new UserInfoBo(1,"USERNAME", true, "PASSWORD_ENCRIPTED" ));
        when(passwordEncryptor.matches(any(), any())).thenReturn(true);
        when(generateToken.generateTokens(any(), any())).thenReturn(new JWTokenBo("TOKEN", "REFRESH_TOKEN"));
        var result = login.execute(new LoginBo("USERNAME", "PASSWORD"));
        assertEquals("TOKEN", result.token);
        assertEquals("REFRESH_TOKEN", result.refreshToken);
    }
}