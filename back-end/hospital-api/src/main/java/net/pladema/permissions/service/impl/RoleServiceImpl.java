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
			case ROOT: return "ROOT";
			case ADMINISTRADOR: return "Administrador";
			case ESPECIALISTA_MEDICO: return "Especialista Médico";
			case PROFESIONAL_DE_SALUD: return "Profesional de la salud";
			case ADMINISTRATIVO: return "Administrativo";
		}
		throw new NotFoundException("role-not-exists", String.format("El rol %s no existe", eRole));
	}
}
