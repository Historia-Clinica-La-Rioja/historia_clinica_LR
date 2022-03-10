package net.pladema.permissions.service;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.session.application.port.UserSessionStorage;

public interface LoggedUserService {

	/**
	 * Retorna el ID del usuario que hizo el request
	 * @deprecated
	 * <p> Use {@link UserSessionStorage#getUserId()} instead.
	 */
	@Deprecated
	Integer getUserId();

	/**
	 * Retorna los roles usuario que hizo el request
	 * @deprecated
	 * <p> Use {@link UserSessionStorage#getRolesAssigned()} instead.
	 */
	Stream<RoleAssignment> currentAssignments();

	default Function<Integer, Boolean> hasAnyInstitutionRole(ERole... roles) {
		return (Integer institucion) -> currentAssignments()
				.anyMatch(roleAssignmentAuthority -> Arrays.stream(roles).anyMatch(
						role -> roleAssignmentAuthority.isAssigment(role, institucion)
				));
	}
}
