package ar.lamansys.sgx.auth.jwt.infrastructure.output.twofactorauthentication;

import ar.lamansys.sgx.auth.jwt.application.logintwofactorauthentication.TwoFactorAuthenticationStorage;

import ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.input.service.TwoFactorAuthenticationExternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwoFactorAuthenticationStorageImpl implements TwoFactorAuthenticationStorage {

	private final TwoFactorAuthenticationExternalService twoFactorAuthenticationExternalService;

	@Override
	public Boolean verifyCode(String code) {
		return twoFactorAuthenticationExternalService.verifyCode(code);
	}
}
