package net.pladema.permissions.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import lombok.EqualsAndHashCode;
import net.pladema.permissions.repository.enums.ERole;
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
