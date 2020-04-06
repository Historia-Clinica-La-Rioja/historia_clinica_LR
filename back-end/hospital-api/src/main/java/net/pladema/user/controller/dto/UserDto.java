package net.pladema.user.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDto extends AbstractUserDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1314611830338628432L;

	@NotNull(message = "{email.mandatory}")
	@Email(message = "{email.mandatory}")
	private String email;

	private Integer id;

}
