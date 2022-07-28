package ar.lamansys.sgx.auth.user.application.updateownpassword;

import ar.lamansys.sgx.auth.user.application.updateownpassword.exceptions.PasswordException;
import ar.lamansys.sgx.auth.user.application.updateownpassword.exceptions.PasswordExceptionEnum;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;
import ar.lamansys.sgx.auth.user.application.updateuserpassword.UpdateUserPassword;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordValidator;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateOwnPassword {

	private final UserStorage userStorage;
	private final PasswordEncryptor passwordEncryptor;
	private final UpdateUserPassword updateUserPassword;
	private final PasswordValidator passwordValidator;

	public void execute(Integer userId, String oldPassword, String newPassword) {
		UserBo user = userStorage.getUser(userId);
		assertValidUpdate(userId, oldPassword, newPassword, user.getPassword());
		updateUserPassword.run(user, newPassword);
	}

	private void assertValidUpdate (Integer userId, String password, String newPassword, String oldPassword) throws  PasswordException{
		if (userId == null)
			throw new PasswordException(PasswordExceptionEnum.USER_INCORRECT, "El usuario no existe");
		if (!passwordEncryptor.matches(password, oldPassword))
			throw new PasswordException(PasswordExceptionEnum.PASSWORD_INCORRECT, "Contraseña incorrecta");
		if (newPassword == null || newPassword.isEmpty() || !passwordValidator.passwordIsValid(newPassword))
			throw new PasswordException(PasswordExceptionEnum.NEW_PASSWORD_INCORRECT, "Debe contener un mínimo de 8 caracteres, y al menos 1 mayúscula, 1 mínuscula y 1 número.");
	}

}
