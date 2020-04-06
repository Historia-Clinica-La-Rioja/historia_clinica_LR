package net.pladema.security.authentication.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import net.pladema.security.authentication.constraints.EnabledUser;

public class LoginDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8314119814285504143L;

	@NotNull(message = "{username.mandatory}")
	@NotBlank(message = "{username.mandatory}")
	@EnabledUser
	private String username;

	@NotNull(message = "{password.mandatory}")
	@NotBlank(message = "{password.mandatory}")
	private String password;

	public LoginDto() {
		super();
	}

	public LoginDto(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
