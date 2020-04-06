package net.pladema.user.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddUserDto extends AbstractUserDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1314611830338628432L;

	@NotNull(message = "{password.mandatory}")
	@NotBlank(message = "{password.mandatory}")
	private String password;

	@NotNull(message = "{email.mandatory}")
	@Email(message = "{email.mandatory}")
	private String email;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
