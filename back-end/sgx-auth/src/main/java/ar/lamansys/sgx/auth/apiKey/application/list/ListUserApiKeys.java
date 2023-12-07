package ar.lamansys.sgx.auth.apiKey.application.list;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.UserKeyRepository;

import ar.lamansys.sgx.auth.apiKey.domain.UserApiKeyBo;
import lombok.AllArgsConstructor;
import net.pladema.sgx.session.application.port.UserIdStorage;

@AllArgsConstructor
@Service
public class ListUserApiKeys {
	private final UserKeyRepository userKeyRepository;
	private final UserIdStorage userSessionStorage;

	public List<UserApiKeyBo> execute() {
		var loggedUserId = userSessionStorage.getUserId();
		var list = userKeyRepository.findUserKeyByUser(loggedUserId);
		return list.stream()
				.map(UserApiKeyBo::fromEntity)
				.collect(Collectors.toList());
	}
}
