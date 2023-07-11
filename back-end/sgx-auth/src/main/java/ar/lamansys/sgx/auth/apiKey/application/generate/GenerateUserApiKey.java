package ar.lamansys.sgx.auth.apiKey.application.generate;

import java.util.UUID;
import java.util.regex.Pattern;

import ar.lamansys.sgx.auth.apiKey.domain.exceptions.KeyNameCharacterLimitExceededException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.auth.apiKey.domain.exceptions.DuplicateKeyNameException;
import ar.lamansys.sgx.auth.apiKey.domain.GenerateUserApiKeyBo;
import ar.lamansys.sgx.auth.apiKey.domain.exceptions.InvalidKeyNameException;
import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.UserKey;
import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.UserKeyRepository;
import lombok.AllArgsConstructor;
import net.pladema.sgx.session.application.port.UserIdStorage;

@AllArgsConstructor
@Service
public class GenerateUserApiKey {
	private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_ -]*$");
	private static final Integer KEY_NAME_CHARACTER_LIMIT = 255;

	private final UserKeyRepository userKeyRepository;
	private final UserIdStorage userSessionStorage;

	@Transactional
	public GenerateUserApiKeyBo execute(String keyName) {
		validateApiKeyName(keyName);
		var loggedUserId = userSessionStorage.getUserId();
		var userKey = new UserKey();
		userKey.setUserId(loggedUserId);
		userKey.setName(keyName);
		userKey.setKey(createUuid());
		try {
			userKeyRepository.save(userKey);
			userKeyRepository.flush();
		} catch (Exception e) {
			throw new DuplicateKeyNameException(e);
		}
		return new GenerateUserApiKeyBo(
				userKey.getName(),
				userKey.getKey()
		);
	}

	private static void validateApiKeyName(String name) {
		if (name == null || !PATTERN.matcher(name).matches()) {
			throw new InvalidKeyNameException();
		}
		if (name.length() > KEY_NAME_CHARACTER_LIMIT) {
			throw new KeyNameCharacterLimitExceededException();
		}
	}
	private static String createUuid() {
		return UUID.randomUUID().toString();
	}
}
