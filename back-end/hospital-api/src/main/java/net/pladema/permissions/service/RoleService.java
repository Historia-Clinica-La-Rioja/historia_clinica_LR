package net.pladema.permissions.service;

import java.util.List;

import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;

public interface RoleService {

	public List<String> getAuthoritiesClaims(Integer userId);
	
	public List<RoleAssignment> getUserRoleAssignments(Integer userId);
	
	public UserRole createUserRole(Integer userId, ERole eRole);

	void updateAdminRole(Integer userId, ERole admin);

}
