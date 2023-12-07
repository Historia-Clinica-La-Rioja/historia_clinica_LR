package ar.lamansys.sgx.auth.apiKey.domain;

import ar.lamansys.sgx.auth.apiKey.infrastructure.output.repository.userkey.UserKey;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserApiKeyBo {
	public final String name;

	public static UserApiKeyBo fromEntity(UserKey userKey) {
		return new UserApiKeyBo(
			userKey.getName()
		);
	}
}
