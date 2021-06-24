package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto;

import lombok.Getter;

@Getter
public class JWTokenDto {

	public final String token;
	public final String refreshToken;

	public JWTokenDto(String token, String refreshToken) {
		this.token = token;
		this.refreshToken = refreshToken;
	}
}
