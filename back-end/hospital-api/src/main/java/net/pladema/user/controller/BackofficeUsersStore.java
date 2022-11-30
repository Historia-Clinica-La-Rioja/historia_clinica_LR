package net.pladema.user.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
import net.pladema.staff.repository.ProfessionalProfessionRepository;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.user.controller.dto.BackofficeUserDto;
import net.pladema.user.controller.exceptions.BackofficeUserException;
import net.pladema.user.controller.exceptions.BackofficeUserExceptionEnum;
import net.pladema.user.controller.mappers.UserDtoMapper;
import net.pladema.user.repository.UserPersonRepository;
import net.pladema.user.repository.VHospitalUserRepository;
import net.pladema.user.repository.entity.UserPerson;
import net.pladema.user.repository.entity.VHospitalUser;

@Service
public class BackofficeUsersStore implements BackofficeStore<BackofficeUserDto, Integer> {
	private final UserRepository userRepository;

	private final VHospitalUserRepository vHospitalUserRepository;

	private final UserDtoMapper userDtoMapper;

	private final UserPersonRepository userPersonRepository;

	private final List<Integer> administratorUserIds;

	private final UserExternalService userExternalService;

	private final UserRoleRepository userRoleRepository;

	private final UserRoleDtoMapper userRoleDtoMapper;

	@Value("${test.stress.disable.validation:false}")
	private boolean disableValidation;

	public BackofficeUsersStore(UserRepository userRepository,
								VHospitalUserRepository vHospitalUserRepository,
								UserRoleRepository userRoleRepository,
								UserDtoMapper userDtoMapper,
								UserPersonRepository userPersonRepository,
								UserExternalService userExternalService,
								UserRoleDtoMapper userRoleDtoMapper) {
		this.vHospitalUserRepository = vHospitalUserRepository;
		this.userDtoMapper = userDtoMapper;
		this.userRepository = userRepository;
		this.userPersonRepository = userPersonRepository;
		this.administratorUserIds = userRoleRepository.findAllByRoles(List.of(ERole.ROOT.getId()));
		this.userExternalService = userExternalService;
		this.userRoleRepository = userRoleRepository;
		this.userRoleDtoMapper = userRoleDtoMapper;
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

	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
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
		if (dto.getId() != null) {
			return update(dto);
		}
		return create(dto);
	}

	private BackofficeUserDto update(BackofficeUserDto dto) {
		return userRepository.findById(dto.getId())
				.filter(user -> {
					if ((!user.getUsername().equals(dto.getUsername())) &&
							userRepository.findByUsername(dto.getUsername()).isPresent()) {
						throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_ALREADY_EXISTS, "El username ya existe en el sistema");
					}
					return true;
				})
				.map(inDb -> userDtoMapper.toModel(dto, inDb))
				.map(userRepository::save)
				.flatMap(u -> vHospitalUserRepository.findById(u.getId()))
				.map(userDtoMapper::fromVHospitalUserToDto)
				.orElseThrow(() -> new BackofficeUserException(BackofficeUserExceptionEnum.UNEXISTED_USER,
						String.format("El usuario %s no existe", dto.getId())));
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

		userExternalService.enableUser(dto.getUsername());
		userPersonRepository.save(new UserPerson(saved.getId(), dto.getPersonId()));
		return saved;
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
		if(userRepository.findByUsername(userDto.getUsername()).isPresent()) {
			throw new BackofficeUserException(BackofficeUserExceptionEnum.USER_ALREADY_EXISTS, "El username ya existe en el sistema");
		}
	}
}
