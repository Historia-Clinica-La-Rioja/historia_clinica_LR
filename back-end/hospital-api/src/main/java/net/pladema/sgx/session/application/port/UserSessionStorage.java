package net.pladema.sgx.session.application.port;

import java.util.stream.Stream;

import net.pladema.permissions.service.dto.RoleAssignment;

public interface UserSessionStorage extends UserIdStorage {

	/**
	 * Retorna los roles usuario que hizo el request
	 */
	Stream<RoleAssignment> getRolesAssigned();
}
