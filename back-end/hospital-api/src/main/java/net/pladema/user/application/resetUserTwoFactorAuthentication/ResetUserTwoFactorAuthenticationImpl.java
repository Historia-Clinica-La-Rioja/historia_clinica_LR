package net.pladema.user.application.resetUserTwoFactorAuthentication;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.user.application.port.HospitalUserStorage;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResetUserTwoFactorAuthenticationImpl implements ResetUserTwoFactorAuthentication {

	private final HospitalUserStorage hospitalUserStorage;

	@Override
	public void run(Integer userId) {
		log.debug("Input parameter -> userId {}", userId);
		hospitalUserStorage.resetTwoFactorAuthentication(userId);
	}
}
