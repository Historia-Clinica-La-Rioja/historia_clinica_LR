package ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.output;

import ar.lamansys.sgx.auth.twoFactorAuthentication.application.UserAuthenticationStorage;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationStorageImpl implements UserAuthenticationStorage {

	private final UserRepository userRepository;

	@Override
	public String getUsername(Integer userId) {
		log.debug("Input parameter -> userId {}", userId);
		String result = userRepository.getById(userId).getUsername();
		log.debug("Output -> {}", result);
		return result;
	}

	@Override
	public void setTwoFactorAuthenticationSecret(Integer userId, String secret) {
		log.debug("Input parameters -> userId {}, secret {}", userId, secret);
		userRepository.setTwoFactorAuthenticationSecret(userId, secret);
	}

	@Override
	public Optional<String> getTwoFactorAuthenticationSecret(Integer userId) {
		log.debug("Input parameter -> userId {}", userId);
		return userRepository.getTwoFactorAuthenticationSecret(userId);
	}

	@Override
	public void enableTwoFactorAuthentication(Integer userId) {
		log.debug("Input parameters -> userId {}", userId);
		userRepository.enableTwoFactorAuthentication(userId);
	}
}
