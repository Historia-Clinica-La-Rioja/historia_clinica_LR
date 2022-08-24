package ar.lamansys.sgx.auth.user.infrastructure.input.rest.updatepassword.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PasswordDto {

	@NotNull(message = "{password.mandatory}")
	@NotBlank(message = "{password.mandatory}")
	private String password;

	@NotNull(message = "{password.mandatory}")
	@NotBlank(message = "{password.mandatory}")
	private String newPassword;
}
