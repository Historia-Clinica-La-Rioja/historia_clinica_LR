package ar.lamansys.sgx.auth.apiKey.infrastructure.input.rest.dto;

import ar.lamansys.sgx.auth.apiKey.domain.UserApiKeyBo;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class UserApiKeyDto {
	public final String name;

	public static UserApiKeyDto fromBo(UserApiKeyBo userApiKeyBo) {
		return new UserApiKeyDto(
			userApiKeyBo.name
		);
	}
}
