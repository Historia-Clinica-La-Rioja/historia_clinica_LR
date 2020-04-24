package net.pladema.permissions.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.permissions.controller.constraints.ExistRole;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class RequestUserRoleDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull(message = "{userid.mandatory}")
	private Integer userId;

	@ExistRole
	private Short roleId;

}
