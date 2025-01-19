package net.pladema.person.controller;

import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.person.controller.dto.ManagerUserPersonDto;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import net.pladema.user.repository.UserPersonRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeManagerUserPersonStore implements BackofficeStore<ManagerUserPersonDto, Integer> {

	private static final List<Short> MANAGER_ROLES_IDS = List.of(ERole.GESTOR_DE_ACCESO_LOCAL.getId(), ERole.GESTOR_DE_ACCESO_REGIONAL.getId());

	private final UserRoleRepository userRoleRepository;
	private final UserPersonRepository userPersonRepository;
	private final List<Role> roles;

	public BackofficeManagerUserPersonStore(UserRoleRepository userRoleRepository,
											UserPersonRepository userPersonRepository,
											RoleRepository roleRepository){
		this.userRoleRepository = userRoleRepository;
		this.userPersonRepository = userPersonRepository;
		this.roles = roleRepository.findAllById(MANAGER_ROLES_IDS);
	}


	@Override
	public Page<ManagerUserPersonDto> findAll(ManagerUserPersonDto example, Pageable pageable) {
		List<Integer> managerIds = userRoleRepository.findUsersByRoles(MANAGER_ROLES_IDS);
		List<ManagerUserPersonDto> result = userPersonRepository.findAllByUserIds(managerIds).stream().map(ManagerUserPersonDto::new).sorted(Comparator.comparing(ManagerUserPersonDto::getCompleteName)).collect(Collectors.toList());
		if (!example.getSearchText().isEmpty())
			result = result.stream().filter(person -> (person.getCompleteName().toLowerCase().contains(example.getSearchText()) || person.getIdentificationNumber().contains(example.getSearchText()))).collect(Collectors.toList());
		return new PageImpl<>(result, pageable, result.size());
	}

	@Override
	public List<ManagerUserPersonDto> findAll() {
		return Collections.emptyList();
	}

	@Override
	public List<ManagerUserPersonDto> findAllById(List<Integer> ids) {
		List<ManagerUserPersonDto> result = userPersonRepository.findAllByUserIds(ids)
				.stream()
				.map(ManagerUserPersonDto::new)
				.sorted(Comparator.comparing(ManagerUserPersonDto::getCompleteName))
				.collect(Collectors.toList());
		result.forEach(this::addRoleInfo);
		return result;
	}

	@Override
	public Optional<ManagerUserPersonDto> findById(Integer id) {
		return Optional.empty();
	}

	@Override
	public ManagerUserPersonDto save(ManagerUserPersonDto entity) {
		return null;
	}

	@Override
	public void deleteById(Integer id) {

	}

	@Override
	public Example<ManagerUserPersonDto> buildExample(ManagerUserPersonDto entity) {
		return Example.of(entity);
	}

	private void addRoleInfo (ManagerUserPersonDto userPerson){
		List<Short> userRoles = userRoleRepository.findByUserId(userPerson.getId()).stream().map(UserRole::getRoleId).collect(Collectors.toList());
		roles.forEach(role -> {
			if (userRoles.contains(role.getId()))
				userPerson.setRole(role.getDescription());
		});
	}

}
