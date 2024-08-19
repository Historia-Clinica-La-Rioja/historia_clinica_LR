package net.pladema.sgx.session.application.port;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;

public interface UserSessionStorage extends UserIdStorage {

	/**
	 * Retorna los roles usuario que hizo el request
	 */
	Stream<RoleAssignment> getRolesAssigned();

	default Supplier<Boolean> hasAnyRole(ERole... roles) {
		return () -> getRolesAssigned()
				.anyMatch(
						roleAssignmentAuthority -> Arrays.stream(roles)
								.anyMatch(roleAssignmentAuthority::isRole)
				);
	}
}
