package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenDto {
	public final String refreshToken;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public RefreshTokenDto(
			@JsonProperty(value = "refreshToken", required = true) String refreshToken
	) {
		this.refreshToken = refreshToken;
	}

}
