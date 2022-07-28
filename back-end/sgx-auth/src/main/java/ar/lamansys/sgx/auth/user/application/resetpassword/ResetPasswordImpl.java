package ar.lamansys.sgx.auth.user.application.resetpassword;

import ar.lamansys.sgx.auth.user.application.updateownpassword.exceptions.PasswordException;
import ar.lamansys.sgx.auth.user.application.updateownpassword.exceptions.PasswordExceptionEnum;
import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenBo;
import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenStorage;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.application.updateuserpassword.UpdateUserPassword;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordValidator;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordImpl implements ResetPassword {

    private final Logger logger;
    private final UserStorage userStorage;
    private final UpdateUserPassword updateUserPassword;
    private final PasswordResetTokenStorage passwordResetTokenStorage;
	private final PasswordValidator passwordValidator;

	public ResetPasswordImpl(UserStorage userStorage,
							 UpdateUserPassword updateUserPassword,
							 PasswordResetTokenStorage passwordResetTokenStorage,
							 PasswordValidator passwordValidator) {
		this.updateUserPassword = updateUserPassword;
		this.passwordResetTokenStorage = passwordResetTokenStorage;
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.userStorage = userStorage;
		this.passwordValidator = passwordValidator;
	}


    @Override
    public String execute(String token, String password) {
        logger.debug("Reset password from token -> {}", token);

        PasswordResetTokenBo passwordResetTokenBo = passwordResetTokenStorage.get(token);

        UserBo user = userStorage.getUser(passwordResetTokenBo.getUserId());
		assertValidUpdate(password);
		updateUserPassword.run(user, password);
        passwordResetTokenStorage.disableTokens(passwordResetTokenBo.getUserId());
        return user.getUsername();
    }

	private void assertValidUpdate (String password) throws PasswordException {
		if (password == null || password.isEmpty() || !passwordValidator.passwordIsValid(password)) {
			throw new PasswordException(PasswordExceptionEnum.NEW_PASSWORD_INCORRECT, "Debe contener un mínimo de 8 caracteres, y al menos\n" + "1 mayúscula, 1 mínuscula y 1 número.");
		}
	}
}
