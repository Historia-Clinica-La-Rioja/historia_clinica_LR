package net.pladema.permissions.service.impl;

import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.entity.UserRolePK;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.permissions.service.dto.RoleAssignment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAssignmentServiceImpl implements UserAssignmentService {

	private final UserRoleRepository userRoleRepository;

	public UserAssignmentServiceImpl(
			UserRoleRepository userRoleRepository
	) {
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public List<RoleAssignment> getRoleAssignment(Integer userId) {
		return userRoleRepository.getRoleAssignments(userId);
	}

	@Override
	public void saveUserRole(Integer userId, ERole eRole, Integer institutionId) {
		UserRolePK pk = new UserRolePK(userId, eRole.getId());
		if (!userRoleRepository.findById(pk).isPresent()) {
			userRoleRepository.save(new UserRole(pk.getUserId(), pk.getRoleId(), institutionId));
		}
	}

	@Override
	public void removeAllPermissions(Integer userId) {
		userRoleRepository.deleteByUserId(userId);
	}

}
