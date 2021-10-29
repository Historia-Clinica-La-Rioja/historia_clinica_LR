package ar.lamansys.sgx.auth.user.infrastructure.input.rest.passwordreset.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class PasswordResetResponseDto {
	private final String username;
}
