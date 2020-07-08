package net.pladema.permissions.service.impl;


import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.permissions.service.domain.UserBo;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.user.repository.UserRepository;
import net.pladema.user.repository.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoggedUserServiceImpl implements LoggedUserService {

	private final UserRoleRepository userRoleRepository;
	private final UserRepository userRepository;

	public LoggedUserServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository) {
		this.userRoleRepository = userRoleRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Integer getUserId() {
		return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Override
	public List<RoleAssignment> getPermissionAssignment() {
		return userRoleRepository.getRoleAssignments(getUserId());
	}

	@Override
	public UserBo getInfo() {
		User user = userRepository.findById(this.getUserId()).get();
		return new UserBo(user);
	}
}
