package ar.lamansys.sgx.auth.user.application.resetpassword;

import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenBo;
import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenStorage;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.domain.userpassword.UpdateUserPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordImpl implements ResetPassword {

    private final Logger logger;

    private final UserStorage userStorage;

    private final UpdateUserPassword updateUserPassword;

    private final PasswordResetTokenStorage passwordResetTokenStorage;

    public ResetPasswordImpl(UserStorage userStorage,
                             UpdateUserPassword updateUserPassword, PasswordResetTokenStorage passwordResetTokenStorage) {
        this.updateUserPassword = updateUserPassword;
        this.passwordResetTokenStorage = passwordResetTokenStorage;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.userStorage = userStorage;
    }

    @Override
    public String execute(String token, String password) {
        logger.debug("Reset password from token -> {}", token);

        PasswordResetTokenBo passwordResetTokenBo = passwordResetTokenStorage.get(token);

        UserBo user = userStorage.getUser(passwordResetTokenBo.getUserId());

        updateUserPassword.run(user, password);

        passwordResetTokenStorage.disableTokens(passwordResetTokenBo.getUserId());

        return user.getUsername();
    }
}
