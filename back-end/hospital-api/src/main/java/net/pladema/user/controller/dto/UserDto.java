package net.pladema.user.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.annotation.Nullable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserDto extends AbstractUserDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1314611830338628432L;

	@NotNull(message = "{email.mandatory}")
	@Email(message = "{email.mandatory}")
	private String email;

	private Integer id;

	@Nullable
	private UserPersonDto personDto;
}
