package net.pladema.authorization.infrastructure.input.rest.mapper;

import net.pladema.permissions.repository.enums.ERole;

public interface RoleNameMapper {

	/**
	 * Se usa la descripción user-friendly del rol para mostrar en pantalla.
	 */
	String getRoleDescription(ERole eRole);

}
