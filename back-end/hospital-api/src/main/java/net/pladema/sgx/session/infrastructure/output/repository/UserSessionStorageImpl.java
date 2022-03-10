package net.pladema.sgx.session.infrastructure.output.repository;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.permissions.service.RoleAssignmentAuthority;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@Service
public class UserSessionStorageImpl implements UserSessionStorage {
	@Override
	public Integer getUserId() {
		return SecurityContextUtils.getUserDetails().userId;
	}

	@Override
	public Stream<RoleAssignment> getRolesAssigned() {
		return SecurityContextUtils.getAuthentication().getAuthorities().stream()
				.map(grantedAuthority -> (RoleAssignmentAuthority)grantedAuthority)
				.map(RoleAssignmentAuthority::assignment)
				.map(this::toRoleAssignmentBo);
	}

	private RoleAssignment toRoleAssignmentBo(RoleAssignment roleAssignment) {
		return new RoleAssignment(
				roleAssignment.role,
				roleAssignment.institutionId
		);
	}
}
