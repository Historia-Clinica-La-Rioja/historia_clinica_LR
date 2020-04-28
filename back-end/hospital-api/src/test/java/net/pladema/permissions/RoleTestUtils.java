package net.pladema.permissions;

import net.pladema.auditable.entity.Audit;
import net.pladema.permissions.repository.entity.Permission;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.user.repository.entity.User;

import java.time.LocalDateTime;

public class RoleTestUtils {

	private RoleTestUtils() {

	}

	public static Permission createPermission() {
		Permission permission = new Permission();
		permission.setDescription("PRUEBA");
		return permission;
	}

	public static Role createLicense(String description) {
		Role license = new Role();
		license.setDescription(description);
		return license;
	}



	public static UserRole createUserLicense(User user, Role role) {
		UserRole ul = new UserRole(user.getId(), role.getId());
		Audit audit = new Audit();
		audit.setCreatedOn(LocalDateTime.now());
		ul.setAudit(audit);
		return ul;
	}
}
