package ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.input.service;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.validateTwoFactorAuthentication.ValidateTwoFactorAuthenticationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwoFactorAuthenticationExternalService {

	private final ValidateTwoFactorAuthenticationCode validateTwoFactorAuthenticationCode;

	public Boolean verifyCode(String code) {
		log.debug("Input parameter -> code {}", code);
		Boolean result = this.validateTwoFactorAuthenticationCode.run(code);
		log.debug("Output -> {}", result);
		return result;
	}

}
