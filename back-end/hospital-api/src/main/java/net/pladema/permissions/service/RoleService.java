package net.pladema.permissions.service;

import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;

import java.util.List;

public interface RoleService {

	List<String> getAuthoritiesClaims(Integer userId);

	UserRole createUserRole(Integer userId, ERole eRole);

	void updateAdminRole(Integer userId, ERole admin);

}
