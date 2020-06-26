package net.pladema.permissions.service.impl;


import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.dto.RoleAssignment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggedUserServiceImpl implements LoggedUserService {

	private final UserRoleRepository userRoleRepository;

	public LoggedUserServiceImpl(UserRoleRepository userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public Integer getUserId() {
		return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Override
	public List<RoleAssignment> getPermissionAssignment() {
		return userRoleRepository.getRoleAssignments(getUserId());
	}
}
