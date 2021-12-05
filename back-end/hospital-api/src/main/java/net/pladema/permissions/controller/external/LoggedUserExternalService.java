package net.pladema.permissions.controller.external;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;

import java.util.List;
import java.util.function.Function;

public interface LoggedUserExternalService {

	/**
	 * It's best to use {@link #hasAnyRoleInstitution(ERole...)}
	 * @Deprecated
	 */
	@Deprecated
	List<RoleAssignment> getPermissionAssignment();

	/**
	 * It's best to use {@link #hasAnyRoleInstitution(ERole...)}
	 * @Deprecated
	 */
	@Deprecated
	boolean hasAnyRoleInstitution(Integer institutionId, ERole... roles);

	Function<Integer, Boolean> hasAnyRoleInstitution(ERole... roles);

}
