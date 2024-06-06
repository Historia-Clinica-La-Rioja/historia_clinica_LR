package net.pladema.user.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.permissions.repository.enums.ERole;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.user.controller.filters.BackofficeRolesFilter;

@AllArgsConstructor
@Service
public class BackofficeRolesStore implements BackofficeStore<Role, Short> {
	private final RoleRepository roleRepository;
	private final BackofficeRolesFilter backofficeRolesFilter;
	private final BackofficeAuthoritiesValidator backofficeAuthoritiesValidator;
	private final FeatureFlagsService featureFlagsService;

	@Override
	public Page<Role> findAll(Role example, Pageable pageable) {
		List<Role> content = toList(roleRepository.findAll()).stream().filter(backofficeRolesFilter::filterRoles).collect(Collectors.toList());
		if(!backofficeAuthoritiesValidator.hasRole(ERole.ROOT))
			content = content.stream().filter(role -> !role.getId().equals(ERole.ADMINISTRADOR_DE_ACCESO_DOMINIO.getId())).collect(Collectors.toList());
		if (!backofficeAuthoritiesValidator.hasRole(ERole.ADMINISTRADOR) || !featureFlagsService.isOn(AppFeature.HABILITAR_TURNOS_CENTRO_LLAMADO))
			content.removeIf(role -> role.getId().equals(ERole.GESTOR_CENTRO_LLAMADO.getId()));
		if(!backofficeAuthoritiesValidator.hasRole(ERole.ADMINISTRADOR_DE_ACCESO_DOMINIO)){
			content = content.stream().filter(role -> !role.getId().equals(ERole.GESTOR_DE_ACCESO_LOCAL.getId()) &&
					!role.getId().equals(ERole.GESTOR_DE_ACCESO_REGIONAL.getId()) &&
					!role.getId().equals(ERole.GESTOR_DE_ACCESO_DE_DOMINIO.getId())).collect(Collectors.toList());
		} else {
			if (!backofficeAuthoritiesValidator.hasRole(ERole.ROOT) && !backofficeAuthoritiesValidator.hasRole(ERole.ADMINISTRADOR)) {
				content = content.stream().filter(role -> role.getId().equals(ERole.GESTOR_DE_ACCESO_LOCAL.getId()) ||
						role.getId().equals(ERole.GESTOR_DE_ACCESO_REGIONAL.getId()) ||
						role.getId().equals(ERole.GESTOR_DE_ACCESO_DE_DOMINIO.getId())).collect(Collectors.toList());

			}
		}
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_ADMINISTRADOR_DATOS_PERSONALES)) {
			content.removeIf(role -> role.getId().equals(ERole.ADMINISTRADOR_DE_DATOS_PERSONALES.getId()));
		}

		return new PageImpl<>(content, pageable, content.size());
	}

	@Override
	public List<Role> findAll() {
		return toList(roleRepository.findAll());
	}

	@Override
	public List<Role> findAllById(List<Short> ids) {
		return toList(roleRepository.findAllById(ids));
	}

	@Override
	public Optional<Role> findById(Short id) {
		return roleRepository.findById(id);
	}

	@Override
	public Role save(Role entity) {
		return null;
	}

	@Override
	public void deleteById(Short id) {
		// nothing to do
	}

	@Override
	public Example<Role> buildExample(Role entity) {
		return Example.of(entity);
	}

	private static List<Role> toList(Iterable<Role> roles) {
		return StreamSupport
				.stream(roles.spliterator(), false)
				.collect(Collectors.toList());
	}

}
