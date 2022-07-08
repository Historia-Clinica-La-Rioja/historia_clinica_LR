package ar.lamansys.sgx.auth.twoFactorAuthentication.application.loggedUserHasTwoFactorAuthenticationEnabled;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.UserAuthenticationStorage;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoggedUserHasTwoFactorAuthenticationEnabled {

	private final UserAuthenticationStorage userAuthenticationStorage;

	public Boolean run() {
		log.debug("No input parameters");
		Integer userId = UserInfo.getCurrentAuditor();
		Boolean result = userAuthenticationStorage.userHasTwoFactorAuthenticationEnabled(userId);
		log.debug("Output -> {}", result);
		return result;
	}
}
