package ar.lamansys.sgx.auth.user.application.updateUsernameAndPassword;

import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenBo;
import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenStorage;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.OAuthUserManagementStorage;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.application.updateuserpassword.UpdateUserPassword;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordValidator;

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
class UpdateUsernameAndPasswordTest {

    private UpdateUsernameAndPassword updateUsernameAndPassword;

    @Mock
    private UserStorage userStorage;

    @Mock
    private OAuthUserManagementStorage oAuthUserManagementStorage;

    @Mock
    private PasswordResetTokenStorage passwordResetTokenStorage;

    @Mock
    private UpdateUserPassword updateUserPassword;

	private PasswordValidator passwordValidator;


    @BeforeEach
    public void setUp() {
		passwordValidator = new PasswordValidator();
        updateUsernameAndPassword = new UpdateUsernameAndPassword(userStorage, passwordResetTokenStorage, updateUserPassword, oAuthUserManagementStorage, passwordValidator);
    }

    @Test
    @DisplayName("Reset password success")
    void resetPasswordSuccess() {
        UserBo userBo = new UserBo(1, "username", true, "Password123", "salt", "hashAlgoritm", LocalDateTime.of(2020,01,01,10,10), LocalDateTime.of(2020,01,01,01,01));

        when(userStorage.getUser(1))
                .thenReturn(userBo);
        when(passwordResetTokenStorage.get("token"))
                .thenReturn(new PasswordResetTokenBo(1L, "token", 1, false, LocalDateTime.of(2020,01,01,10,10)));
        updateUsernameAndPassword.run("token","username", "Password123");

        verify(passwordResetTokenStorage, times(1)).get("token");
        verify(userStorage, times(1)).getUser(1);
        verify(updateUserPassword, times(1)).run(userBo, "Password123");
    }


}