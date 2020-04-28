package net.pladema.permissions.service.impl;

import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.RoleService;
import net.pladema.sgx.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
	private final RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
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

	/**
	 * Se usa la descripción user-friendly del rol para mostrar en pantalla.
	 * Podría usarse el ERole para obtener una traducción.
	 *
	 * @param eRole
	 * @return
	 */
	private String getRoleDescription(ERole eRole) {
		switch (eRole) {
			case ADMIN: return "Admin";
			case ADMIN_APP: return "Administrador";
			case ADVANCED_USER: return "Especialista Médico";
			case BASIC_USER: return "Profesional de la salud";
		}
		throw new NotFoundException("role-not-exists", String.format("El rol %s no existe", eRole));
	}
}
