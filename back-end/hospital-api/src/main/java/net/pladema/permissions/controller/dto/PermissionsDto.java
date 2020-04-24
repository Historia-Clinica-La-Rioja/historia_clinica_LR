package net.pladema.permissions.controller.dto;

import net.pladema.permissions.service.dto.RoleAssignment;

import java.util.Collection;

public class PermissionsDto {
	public final Collection<RoleAssignment> roleAssignments;
	public PermissionsDto(Collection<RoleAssignment> roleAssignments) {
		this.roleAssignments = roleAssignments;
	}
}