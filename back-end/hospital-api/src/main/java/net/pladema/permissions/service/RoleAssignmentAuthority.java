package net.pladema.permissions.service;

import org.springframework.security.core.GrantedAuthority;

import lombok.EqualsAndHashCode;
import net.pladema.permissions.service.dto.RoleAssignment;

public class RoleAssignmentAuthority implements GrantedAuthority {
	@EqualsAndHashCode.Include
	private final RoleAssignment roleAssignment;

	public RoleAssignmentAuthority(RoleAssignment roleAssignment) {
		this.roleAssignment = roleAssignment;
	}

	@Override
	public String getAuthority() {
		return roleAssignment.role.getValue();
	}

	public RoleAssignment assignment() {
		return this.roleAssignment;
	}
}
