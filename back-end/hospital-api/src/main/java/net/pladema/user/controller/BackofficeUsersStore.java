package net.pladema.user.controller;


import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import net.pladema.permissions.controller.dto.BackofficeUserRoleDto;
import net.pladema.permissions.controller.mappers.UserRoleDtoMapper;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.user.controller.dto.BackofficeUserDto;
import net.pladema.user.controller.mappers.UserDtoMapper;
import net.pladema.user.repository.UserPersonRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeUsersStore implements BackofficeStore<BackofficeUserDto, Integer> {
	private final UserRepository repository;
	private final UserRoleRepository userRoleRepository;
	private final UserDtoMapper userDtoMapper;
	private final UserRoleDtoMapper userRoleDtoMapper;
	private final UserRepository userRepository;
	private final UserPersonRepository userPersonRepository;
	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	private static final UserRole rootRole = new UserRole(null, ERole.ROOT.getId());

	public BackofficeUsersStore(UserRepository repository,
								UserRoleRepository userRoleRepository,
								UserDtoMapper userDtoMapper,
								UserRoleDtoMapper userRoleDtoMapper,
								UserRepository userRepository,
								UserPersonRepository userPersonRepository, HealthcareProfessionalRepository healthcareProfessionalRepository
	) {
		this.repository = repository;
		this.userRoleRepository = userRoleRepository;
		this.userDtoMapper = userDtoMapper;
		this.userRoleDtoMapper = userRoleDtoMapper;
		this.userRepository = userRepository;
		this.userPersonRepository = userPersonRepository;
		this.healthcareProfessionalRepository = healthcareProfessionalRepository;
	}

	@Override
	public Page<BackofficeUserDto> findAll(BackofficeUserDto user, Pageable pageable) {
		User modelUser = userDtoMapper.toModel(user);

		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withIgnorePaths("audit", "enable");

		return repository.findAll(
				Example.of(modelUser, customExampleMatcher),
				PageRequest.of(
						pageable.getPageNumber(),
						pageable.getPageSize(),
						Sort.unsorted()
				)
		).map(userDtoMapper::toDto);
	}

	@Override
	public List<BackofficeUserDto> findAll() {
		return repository.findAll().stream()
				.map(userDtoMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<BackofficeUserDto> findAllById(List<Integer> ids) {
		return repository.findAllById(ids).stream()
				.map(userDtoMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<BackofficeUserDto> findById(Integer id) {
		return repository.findById(id)
				.map(userDtoMapper::toDto)
				.map(this::fillRoles);
	}

	private BackofficeUserDto fillRoles(BackofficeUserDto backofficeUserDto) {
		backofficeUserDto.setRoles(
			userRoleRepository.findByUserId(backofficeUserDto.getId())
					.stream()
					.map(userRoleDtoMapper::toDto)
					.collect(Collectors.toList())
		);
		return backofficeUserDto;
	}

	@Override
	public BackofficeUserDto save(BackofficeUserDto dto) {
		checkValidRoles(dto);
		if (dto.getId() != null) {
			return update(dto);
		}
		return create(dto);
	}

	private BackofficeUserDto update(BackofficeUserDto dto) {
		BackofficeUserDto saved = dto;
		List<UserRole> userRoles = userRoleRepository.findByUserId(dto.getId());

		if (!isRoot(userRoles)) {
			saved = repository.findById(dto.getId())
					.map(inDb -> userDtoMapper.toModel(dto, inDb))
					.map(repository::save)
					.map(userDtoMapper::toDto)
					.orElseThrow(() -> new NotFoundException("user-not-found", String.format("El usuario %s no existe", dto.getId())));
		}


		List<UserRole> rolesDelFront = toModel(dto.getRoles());
		rootRole.setUserId(dto.getId());
		if (isRoot(userRoles)) {
			rolesDelFront.add(rootRole);
		}

		userRoleRepository.deleteAll(roleToDelete(userRoles, rolesDelFront));
		userRoleRepository.saveAll(roleToAdd(dto.getId(), toModel(dto.getRoles()), userRoles));

		return saved;
	}

	private boolean isRoot(List<UserRole> roleList) {
		return roleList.stream().map(UserRole::getRoleId).anyMatch(ERole.ROOT.getId()::equals);
	}

	private BackofficeUserDto create(BackofficeUserDto dto) {
		checkIfUserAlreadyExists(dto);
		User modelUser = userDtoMapper.toModel(dto);
		modelUser.setEnable(true);

		BackofficeUserDto saved = userDtoMapper.toDto(repository.save(modelUser));

		userRoleRepository.saveAll(roleToAdd(dto.getId(), toModel(dto.getRoles()), new ArrayList<>()));
		
		return saved;
	}

	protected List<UserRole> roleToDelete(List<UserRole> userRoles, List<UserRole> roleIds) {
		return userRoles.stream()
				.filter(userRole ->
						roleIds.stream().noneMatch(userRole::equals)
				)
				.collect(Collectors.toList());
	}

	protected List<UserRole> roleToAdd(Integer userId, List<UserRole> newRoles, List<UserRole> userRoles) {
		return newRoles.stream()
				.filter(newRole ->
						userRoles.stream().noneMatch(
								userRole -> userRole.equals(newRole)
						)
				)
				.map(newRole -> new UserRole(userId, newRole.getRoleId(), newRole.getInstitutionId()))
				.collect(Collectors.toList());
	}



	private List<UserRole> toModel(List<BackofficeUserRoleDto> roles) {
		return roles
			.stream()
			.map(userRoleDtoMapper::toModel)
			.collect(Collectors.toList());
	}
	
	@Override
	public void deleteById(Integer id) {
		repository.changeStatusAccount(id, false);
	}

	@Override
	public Example<BackofficeUserDto> buildExample(BackofficeUserDto entity) {
		return Example.of(entity);
	}

	private void checkIfUserAlreadyExists(BackofficeUserDto userDto){
		if(userPersonRepository.existsByPersonId(userDto.getPersonId())) {
			throw new BackofficeValidationException("user.exists");
		}
	}

	private void checkValidRoles(BackofficeUserDto dto) {
		if(!dto.getRoles().stream().allMatch(role -> isValidRole(role, dto.getPersonId())))
			throw new BackofficeValidationException("role.requiresprofessional");
	}

	private boolean isValidRole(BackofficeUserRoleDto role, Integer personId) {
		if(!isProfessional(role))
			return true;
		return healthcareProfessionalRepository.findProfessionalByPersonId(personId).isPresent();
	}

	private boolean isProfessional(BackofficeUserRoleDto role) {
		Short roleId = role.getRoleId();
		return ERole.ENFERMERO.getId().equals(roleId) ||
			   ERole.ESPECIALISTA_MEDICO.getId().equals(roleId) ||
			   ERole.ENFERMERO_ADULTO_MAYOR.getId().equals(roleId) ||
			   ERole.PROFESIONAL_DE_SALUD.getId().equals(roleId) ||
			   ERole.ESPECIALISTA_EN_ODONTOLOGIA.getId().equals(roleId);
	}
}
