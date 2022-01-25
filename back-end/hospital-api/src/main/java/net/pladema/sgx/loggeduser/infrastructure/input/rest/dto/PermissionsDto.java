package net.pladema.sgx.loggeduser.infrastructure.input.rest.dto;

import java.util.Collection;

import net.pladema.permissions.service.dto.RoleAssignment;

public class PermissionsDto {
	public final Collection<RoleAssignment> roleAssignments;
	public PermissionsDto(Collection<RoleAssignment> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}
}