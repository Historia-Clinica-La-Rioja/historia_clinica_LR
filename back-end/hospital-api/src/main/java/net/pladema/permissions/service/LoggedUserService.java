package net.pladema.permissions.service;

import net.pladema.permissions.service.domain.UserBo;
import net.pladema.permissions.service.dto.RoleAssignment;

import java.util.List;

public interface LoggedUserService {
	Integer getUserId();

	List<RoleAssignment> getPermissionAssignment();

	UserBo getInfo();
}
