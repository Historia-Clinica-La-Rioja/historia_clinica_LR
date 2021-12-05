package net.pladema.permissions.service;

import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.domain.LoggedUserBo;
import net.pladema.permissions.service.dto.RoleAssignment;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface LoggedUserService {
	Integer getUserId();

	List<RoleAssignment> getPermissionAssignment();

	LoggedUserBo getInfo();

	default Stream<RoleAssignment> currentAssignments() {
		return SecurityContextUtils.getAuthentication().getAuthorities().stream()
				.map(grantedAuthority -> (RoleAssignmentAuthority)grantedAuthority)
				.map(RoleAssignmentAuthority::assignment);
	}

	default Supplier<Boolean> hasAnySystemRole(ERole... roles) {
		return () -> currentAssignments().anyMatch(roleAssignmentAuthority -> Arrays.stream(roles).anyMatch(roleAssignmentAuthority::isRole));
	}

	default Function<Integer, Boolean> hasAnyInstitutionRole(ERole... roles) {
		return (Integer institucion) -> currentAssignments()
				.anyMatch(roleAssignmentAuthority -> Arrays.stream(roles).anyMatch(
						role -> roleAssignmentAuthority.isAssigment(role, institucion)
				));
	}
}
