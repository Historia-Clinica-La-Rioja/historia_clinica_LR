package ar.lamansys.sgx.auth.user.application.restorepasswordbyusername;

import ar.lamansys.sgx.auth.user.application.port.RestorePasswordNotification;
import ar.lamansys.sgx.auth.user.domain.notification.RestorePasswordNotificationBo;
import ar.lamansys.sgx.auth.user.domain.passwordreset.PasswordResetTokenStorage;
import ar.lamansys.sgx.auth.user.domain.user.model.UserBo;
import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestorePasswordByUsername {

	private final UserStorage userStorage;
	private final RestorePasswordNotification restorePasswordNotification;
	private final PasswordResetTokenStorage passwordResetTokenStorage;

	private static final String REGEX = "[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~<>;-]";

	public String execute(String username){
		log.debug("Input parameters -> username {}", username);
		UserBo user = userStorage.getUser(username);
		var token = passwordResetTokenStorage.createToken(user.getId());
		String result = restorePasswordNotification.run(new RestorePasswordNotificationBo(user.getId(), token.getToken()));
		return result.substring(0, result.indexOf("@")-4).replaceAll(REGEX, "*")
				+ result.substring(result.indexOf("@")-4, result.length());
	}

}
