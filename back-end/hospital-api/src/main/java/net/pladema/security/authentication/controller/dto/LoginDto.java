package net.pladema.security.authentication.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LoginDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8314119814285504143L;

	@NotNull(message = "{username.mandatory}")
	@NotBlank(message = "{username.mandatory}")
	public final String username;

	@NotNull(message = "{password.mandatory}")
	@NotBlank(message = "{password.mandatory}")
	public final String password;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public LoginDto(
			@JsonProperty("username") String username,
			@JsonProperty("password") String password
	) {
		this.username = username;
		this.password = password;
	}

}
