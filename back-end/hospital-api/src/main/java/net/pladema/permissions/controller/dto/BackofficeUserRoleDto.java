package net.pladema.permissions.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BackofficeUserRoleDto {

	private Integer institutionId;
	private Short roleId;
	private Integer userId;
}
