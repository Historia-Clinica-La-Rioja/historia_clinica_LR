package net.pladema.permissions.service.impl;

import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.entity.UserRolePK;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.UserAssignmentService;
import net.pladema.permissions.service.dto.RoleAssignment;
import net.pladema.sgx.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserAssignmentServiceImpl implements UserAssignmentService {
	private static final String INVALID_ROLE = "role.invalid";

	private final UserRoleRepository userRoleRepository;
	private final RoleRepository roleRepository;

	public UserAssignmentServiceImpl(
			UserRoleRepository userRoleRepository,
			RoleRepository roleRepository
	) {
		this.userRoleRepository = userRoleRepository;
		this.roleRepository = roleRepository;
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

}
