package net.pladema.sgx.session.infrastructure.input.rest.dto;

import net.pladema.permissions.repository.enums.ERole;

public class RoleAssignmentDto {
	public final ERole role;
	public final String roleDescription;
	public final Integer institutionId;

	public RoleAssignmentDto(ERole role, String roleDescription, Integer institutionId) {
		this.role = role;
		this.roleDescription = roleDescription;
		this.institutionId = institutionId;
	}
}
