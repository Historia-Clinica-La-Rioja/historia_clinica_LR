package ar.lamansys.sgx.auth.apiKey.application.delete;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.UserKeyRepository;
import lombok.AllArgsConstructor;
import net.pladema.sgx.session.application.port.UserIdStorage;

@AllArgsConstructor
@Service
public class DeleteUserApiKey {
	private final UserKeyRepository userKeyRepository;
	private final UserIdStorage userSessionStorage;

	@Transactional
	public void execute(String keyName) {
		var loggedUserId = userSessionStorage.getUserId();

		userKeyRepository.deleteByNameAndUserId(keyName, loggedUserId);
		userKeyRepository.flush();

	}
}
