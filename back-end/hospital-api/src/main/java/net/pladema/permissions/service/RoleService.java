package net.pladema.permissions.service;

import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;

import java.util.List;

public interface RoleService {

	public List<String> getAuthoritiesClaims(Integer userId);

	public UserRole createUserRole(Integer userId, ERole eRole);

	public UserRole createUserRole(Integer userId, Short roleId);

	public void saveRoles(List<Role> roles);

	public boolean existRole(String role);

	public boolean existRole(Short roleId);

	public void updateAdminRole(Integer userId, ERole admin);

	void deleteByUserId(Integer userId);
}
