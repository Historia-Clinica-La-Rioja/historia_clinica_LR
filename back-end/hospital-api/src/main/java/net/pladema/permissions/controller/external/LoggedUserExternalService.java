package net.pladema.permissions.controller.external;

import java.util.function.Function;

import net.pladema.permissions.repository.enums.ERole;

public interface LoggedUserExternalService {

	/**
	 * It's best to use {@link #hasAnyRoleInstitution(ERole...)}
	 * @Deprecated
	 */
	@Deprecated
	boolean hasAnyRoleInstitution(Integer institutionId, ERole... roles);

	Function<Integer, Boolean> hasAnyRoleInstitution(ERole... roles);

}
