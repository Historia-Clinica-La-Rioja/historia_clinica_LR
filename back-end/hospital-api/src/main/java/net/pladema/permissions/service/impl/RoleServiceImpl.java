package net.pladema.permissions.service.impl;

import net.pladema.authorization.infrastructure.input.rest.mapper.RoleNameMapper;
import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.RoleService;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
	private final RoleRepository roleRepository;
	private final RoleNameMapper roleNameMapper;

	public RoleServiceImpl(RoleRepository roleRepository, RoleNameMapper roleNameMapper) {
		this.roleRepository = roleRepository;
		this.roleNameMapper = roleNameMapper;
	}


	@Override
	public void updateRolesStore() {
		Arrays.stream(ERole.values()).forEach(this::updateRole);
	}

	private void updateRole(ERole eRole) {
		Optional<Role> roleInDb = roleRepository.findById(eRole.getId());

		Role role = roleInDb.orElseGet(() -> new Role(eRole.getId(), null));
		String description = getRoleDescription(eRole);
		if (!description.equals(role.getDescription())) {
			role.setDescription(description);
			roleRepository.save(role);
		}
	}

	@Override
	public String getRoleDescription(ERole eRole) {
		return roleNameMapper.getRoleDescription(eRole);
	}

}
