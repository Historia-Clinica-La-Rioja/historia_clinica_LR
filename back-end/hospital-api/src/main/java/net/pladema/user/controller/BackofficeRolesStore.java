package net.pladema.user.controller;

import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BackofficeRolesStore implements BackofficeStore<Role, Short> {
	private final RoleRepository roleRepository;

	public BackofficeRolesStore(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Page<Role> findAll(Role example, Pageable pageable) {
		List<Role> content = toList(roleRepository.findAll()).stream().filter(this::filterRoles).collect(Collectors.toList());
		return new PageImpl<>(content, pageable, content.size());
	}

	private boolean filterRoles(Role role) {
		return !role.getId().equals(ERole.ROOT.getId()) &&
				!role.getId().equals(ERole.API_CONSUMER.getId()) &&
				!role.getId().equals(ERole.PARTIALLY_AUTHENTICATED.getId());
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
