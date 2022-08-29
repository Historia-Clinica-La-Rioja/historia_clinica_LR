package ar.lamansys.sgx.auth.user.application.updateUsernameAndPassword;

import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenBo;
import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenStorage;
import ar.lamansys.sgx.auth.user.domain.user.model.OAuthUserBo;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.OAuthUserManagementStorage;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.application.updateuserpassword.UpdateUserPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateUsernameAndPassword {

    @Value("${ws.oauth.enabled:false}")
    private boolean oAuthServiceEnabled;

    private final UserStorage userStorage;
    private final PasswordResetTokenStorage passwordResetTokenStorage;
    private final UpdateUserPassword updateUserPassword;
    private final OAuthUserManagementStorage oAuthUserManagementStorage;

    public void run(String token, String username, String password) {
        log.debug("Input -> username {}", username);
        PasswordResetTokenBo passwordResetTokenBo = passwordResetTokenStorage.get(token);
        UserBo user = updateUsername(username, passwordResetTokenBo.getUserId());
        updateUserPassword.run(user, password);
        passwordResetTokenStorage.disableTokens(passwordResetTokenBo.getUserId());
    }

    private UserBo updateUsername(String username, Integer userId) {
        UserBo user = userStorage.getUser(userId);
        if (oAuthServiceEnabled)
            oAuthUserManagementStorage.updateUser(user.getUsername(), new OAuthUserBo(username, null, null, null, null));
        user.setUsername(username);
        userStorage.update(user);
        return user;
    }

}
