package net.pladema.permissions.service;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.domain.LoggedUserBo;
import net.pladema.permissions.service.dto.RoleAssignment;

import java.util.List;

public interface LoggedUserService {
	Integer getUserId();

	List<RoleAssignment> getPermissionAssignment();

	LoggedUserBo getInfo();

	boolean hasAnyRoleInstitution(Integer institutionId, List<ERole> role);
}
