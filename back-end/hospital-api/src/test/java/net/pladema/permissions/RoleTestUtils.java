package net.pladema.permissions;

import java.time.LocalDateTime;

import net.pladema.auditable.entity.Audit;
import net.pladema.permissions.controller.dto.RequestUserRoleDto;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.entity.Permission;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.user.repository.entity.User;

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

	public static RequestUserRoleDto validRequestUserLicense() {
		RequestUserRoleDto result = new RequestUserRoleDto();
		result.setRoleId((short) 1);
		result.setUserId(1);
		return result;
	}

	public static RequestUserRoleDto invalidIdLicense() {
		RequestUserRoleDto result = validRequestUserLicense();
		result.setRoleId((short) -1);
		return result;
	}

	public static RequestUserRoleDto nullIdLicense() {
		RequestUserRoleDto result = validRequestUserLicense();
		result.setRoleId(null);
		return result;
	}

	public static RequestUserRoleDto nullIdUser() {
		RequestUserRoleDto result = validRequestUserLicense();
		result.setUserId(null);
		return result;
	}

	public static UserRole createUserLicense(User user, Role role) {
		UserRole ul = new UserRole(user.getId(), role.getId());
		Audit audit = new Audit();
		audit.setCreatedOn(LocalDateTime.now());
		ul.setAudit(audit);
		return ul;
	}
}
