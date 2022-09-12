package ar.lamansys.sgx.auth.twoFactorAuthentication.application.confirmTwoFactorAuthentication;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.UserAuthenticationStorage;
import ar.lamansys.sgx.auth.twoFactorAuthentication.application.validateTwoFactorAuthentication.ValidateTwoFactorAuthenticationCode;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmTwoFactorAuthentication {

	private final ValidateTwoFactorAuthenticationCode validateTwoFactorAuthenticationCode;
	private final UserAuthenticationStorage userAuthenticationStorage;

	public Boolean run(String code) {
		log.debug("Input parameter -> code {}", code);
		Boolean validated = validateTwoFactorAuthenticationCode.run(code);
		if (!validated) {
			return false;
		}
		userAuthenticationStorage.enableTwoFactorAuthentication(UserInfo.getCurrentAuditor());
		return true;
	}
}

