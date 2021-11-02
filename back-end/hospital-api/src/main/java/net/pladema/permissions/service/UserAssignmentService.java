package net.pladema.permissions.service;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;

import java.util.List;

public interface UserAssignmentService {
	List<RoleAssignment> getRoleAssignment(Integer userId);

	void saveUserRole(Integer userId, ERole role, Integer institutionId);

	void removeAllPermissions(Integer userId);
}
