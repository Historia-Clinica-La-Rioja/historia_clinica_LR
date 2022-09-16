package net.pladema.user.controller;


import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import net.pladema.permissions.controller.dto.BackofficeUserRoleDto;
import net.pladema.permissions.controller.mappers.UserRoleDtoMapper;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.user.controller.dto.BackofficeUserDto;
import net.pladema.user.controller.exceptions.BackofficeUserException;
import net.pladema.user.controller.exceptions.BackofficeUserExceptionEnum;
import net.pladema.user.controller.mappers.UserDtoMapper;
import net.pladema.user.repository.UserPersonRepository;
import net.pladema.user.repository.VHospitalUserRepository;
import net.pladema.user.repository.entity.UserPerson;
import net.pladema.user.repository.entity.VHospitalUser;
import net.pladema.staff.repository.ProfessionalProfessionRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeUsersStore implements BackofficeStore<BackofficeUserDto, Integer> {
	private final UserRepository userRepository;

	private final VHospitalUserRepository vHospitalUserRepository;

	private final UserRoleRepository userRoleRepository;

	private final UserDtoMapper userDtoMapper;

	private final UserRoleDtoMapper userRoleDtoMapper;

	private final UserPersonRepository userPersonRepository;

	private final HealthcareProfessionalRepository healthcareProfessionalRepository;

	private final ProfessionalProfessionRepository professionalProfessionRepository;

	private final List<Integer> administratorUserIds;

	private final UserExternalService userExternalService;

	private static final UserRole rootRole = new UserRole(null, ERole.ROOT.getId());

	@Value("${test.stress.disable.validation:false}")
	private boolean disableValidation;

	public BackofficeUsersStore(UserRepository userRepository,
								VHospitalUserRepository vHospitalUserRepository, UserRoleRepository userRoleRepository,
								UserDtoMapper userDtoMapper,
								UserRoleDtoMapper userRoleDtoMapper,
								UserPersonRepository userPersonRepository,
								HealthcareProfessionalRepository healthcareProfessionalRepository,
								UserExternalService userExternalService,
								ProfessionalProfessionRepository professionalProfessionRepository) {
		this.vHospitalUserRepository = vHospitalUserRepository;
		this.userRoleRepository = userRoleRepository;
		this.userDtoMapper = userDtoMapper;
		this.userRoleDtoMapper = userRoleDtoMapper;
		this.userRepository = userRepository;
		this.userPersonRepository = userPersonRepository;
		this.healthcareProfessionalRepository = healthcareProfessionalRepository;
		this.administratorUserIds = userRoleRepository.findAllByRoles(List.of(ERole.ROOT.getId()));
		this.userExternalService = userExternalService;
		this.professionalProfessionRepository = professionalProfessionRepository;
	}

	@Override
	public Page<BackofficeUserDto> findAll(BackofficeUserDto user, Pageable pageable) {
		VHospitalUser modelUser = userDtoMapper.toVHospitalUser(user);

		ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("personId", ExampleMatcher.GenericPropertyMatchers.exact())
				.withIgnorePaths("audit", "enable");

		List<BackofficeUserDto> result = vHospitalUserRepository.findAll(Example.of(modelUser, customExampleMatcher)).stream()
				.map(userDtoMapper::fromVHospitalUserToDto)
				.collect(Collectors.toList());
		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(result.subList(minIndex, Math.min(maxIndex, result.size())), pageable, result.size());
	}

	private boolean infrastructureUsers(VHospitalUser user) {
		return !administratorUserIds.contains(user.getUserId());
	}

	@Override
	public List<BackofficeUserDto> findAll() {
		return vHospitalUserRepository.findAll().stream()
				.filter(this::infrastructureUsers)
				.map(userDtoMapper::fromVHospitalUserToDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<BackofficeUserDto> findAllById(List<Integer> ids) {
		return vHospitalUserRepository.findAllById(ids).stream()
				.map(userDtoMapper::fromVHospitalUserToDto)
				.collect(Collectors.toList());
	}

	@Override
	public Optional<BackofficeUserDto> findById(Integer id) {
		return vHospitalUserRepository.findById(id)
				.map(userDtoMapper::fromVHospitalUserToDto)
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
			saved = userRepository.findById(dto.getId())
					.map(inDb -> userDtoMapper.toModel(dto, inDb))
					.map(userRepository::save)
					.map(u -> vHospitalUserRepository.findById(u.getId()).get())
					.map(userDtoMapper::fromVHospitalUserToDto)
					.orElseThrow(() -> new BackofficeUserException(BackofficeUserExceptionEnum.UNEXISTED_USER,
							String.format("El usuario %s no existe", dto.getId())));
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
		if (!disableValidation)
			checkIfUserAlreadyExists(dto);
		User modelUser = userDtoMapper.toModel(dto);
		modelUser.setEnable(true);

		userExternalService.registerUser(dto.getUsername(), null, null);

		BackofficeUserDto saved = userExternalService.getUser(dto.getUsername())
				.map(userDtoMapper::toDto)
				.orElseThrow(() -> new BackofficeUserException(BackofficeUserExceptionEnum.UNEXISTED_USER, String.format("El usuario %s no existe", dto.getUsername())));

		userRoleRepository.saveAll(roleToAdd(saved.getId(), toModel(dto.getRoles()), new ArrayList<>()));
		if (dto.getPersonId() != null)
			userPersonRepository.save(new UserPerson(saved.getId(), dto.getPersonId()));
		userExternalService.enableUser(dto.getUsername());
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
				.map(ur -> createUserRole(userId,ur))
				.collect(Collectors.toList());
	}

	private UserRole createUserRole(Integer userId, UserRole userRole) {
		UserRole result = new UserRole(userId, userRole.getRoleId(), userRole.getInstitutionId());
		result.setDeleted(false);
		return result;
	}

	private List<UserRole> toModel(List<BackofficeUserRoleDto> roles) {
		return roles
			.stream()
			.map(userRoleDtoMapper::toModel)
			.collect(Collectors.toList());
	}
	
	@Override
	public void deleteById(Integer id) {
		userRepository.changeStatusAccount(id, false);
	}

	@Override
	public Example<BackofficeUserDto> buildExample(BackofficeUserDto entity) {
		return Example.of(entity);
	}

	private void checkIfUserAlreadyExists(BackofficeUserDto userDto){
		if(userPersonRepository.existsByPersonId(userDto.getPersonId())) {
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_ALREADY_EXISTS, "La persona ya contiene un usuario en el sistema");
		}
	}

	private void checkValidRoles(BackofficeUserDto dto) {
		dto.getRoles().stream().filter(role -> !isValidRole(role, dto.getPersonId())).findAny()
				.ifPresent(backofficeUserRoleDto -> {
					String role = ERole.map(backofficeUserRoleDto.getRoleId()).getValue();
					throw new BackofficeUserException(BackofficeUserExceptionEnum.PROFESSIONAL_REQUIRED,
							String.format("El rol %s asignado requiere que el usuario sea un profesional", role));
				});


		if(isROOTUser(dto.getUsername()) && !hasRootRole(dto.getRoles()))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.ROOT_LOST_PERMISSION, "El admin no puede perder el rol: ROOT");
		if(!isROOTUser(dto.getUsername()) && hasRootRole(dto.getRoles()))
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_INVALID_ROLE, "El usuario creado no puede tener el siguiente rol: ROOT");
	}

	private boolean isROOTUser(String username) {
		return "admin@example.com".equals(username);
	}
	private boolean hasRootRole(List<BackofficeUserRoleDto> roles) {
		return List.of(ERole.ROOT).stream().anyMatch(eRole -> existRole(eRole, roles));
	}

	private boolean existRole(ERole eRole, List<BackofficeUserRoleDto> roles) {
		return roles.stream().anyMatch(ur -> eRole.getId().equals(ur.getRoleId()));
	}

	private boolean isValidRole(BackofficeUserRoleDto role, Integer personId) {
		if(!isProfessional(role))
			return true;
		return healthcareProfessionalRepository.findProfessionalByPersonId(personId).map(hp-> professionalProfessionRepository.countActiveByHealthcareProfessionalId(hp)>0)
                .orElse(false);
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
