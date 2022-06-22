package ar.lamansys.sgx.auth.user.application.resettwofactorauthentication;

import ar.lamansys.sgx.auth.user.domain.user.service.UserStorage;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetTwoFactorAuthenticationImpl implements ResetTwoFactorAuthentication {

	private final UserStorage userStorage;

	@Override
	public void run(Integer userId) {
		log.debug("Input parameter -> userId {}", userId);
		userStorage.resetTwoFactorAuthentication(userId);
	}
}
