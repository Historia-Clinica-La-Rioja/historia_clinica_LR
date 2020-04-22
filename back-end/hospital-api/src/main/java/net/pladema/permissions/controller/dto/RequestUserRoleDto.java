package net.pladema.permissions.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.permissions.controller.constraints.ExistRole;
//import net.pladema.user.controller.constraints.ValidUser;

@Getter
@Setter
@NoArgsConstructor
public class RequestUserRoleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "{userid.mandatory}")
//	@ValidUser
	private Integer userId;

	@ExistRole
	private Short roleId;

}
