package net.pladema.permissions;

import net.pladema.permissions.repository.entity.Permission;

public class RoleTestUtils {

	private RoleTestUtils() {

	}

	public static Permission createPermission() {
		Permission permission = new Permission();
		permission.setDescription("PRUEBA");
		return permission;
	}
}
