package ar.lamansys.sgx.auth.user.application.resetpassword;

import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenBo;
import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenStorage;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.domain.userpassword.UpdateUserPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetPasswordImplTest {

    private ResetPassword resetPassword;

    @Mock
    private UserStorage userStorage;

    @Mock
    private UpdateUserPassword updateUserPassword;

    @Mock
    private PasswordResetTokenStorage passwordResetTokenStorage;


    @BeforeEach
    public void setUp() {
        resetPassword = new ResetPasswordImpl(userStorage, updateUserPassword, passwordResetTokenStorage);
    }

    @Test
    @DisplayName("Reset password success")
    void resetPasswordSuccess() {
        UserBo userBo = new UserBo(1, "username", true, "password", "salt", "hashAlgoritm", LocalDateTime.of(2020,01,01,10,10));
        when(userStorage.getUser(1))
                .thenReturn(userBo);
        when(passwordResetTokenStorage.get("token"))
                .thenReturn(new PasswordResetTokenBo(1L, "token", 1, false, LocalDateTime.of(2020,01,01,10,10)));
        resetPassword.execute("token","password");

        verify(passwordResetTokenStorage, times(1)).get("token");
        verify(userStorage, times(1)).getUser(1);
        verify(updateUserPassword, times(1)).run(userBo, "password");
        verify(passwordResetTokenStorage, times(1)).disableTokens(1);
    }
}