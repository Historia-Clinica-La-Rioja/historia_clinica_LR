package net.pladema.sgx.session.infrastructure.input.rest.dto;

import java.util.Collection;

public class PermissionsDto {
	public final Collection<RoleAssignmentDto> roleAssignments;
	public PermissionsDto(Collection<RoleAssignmentDto> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}
}