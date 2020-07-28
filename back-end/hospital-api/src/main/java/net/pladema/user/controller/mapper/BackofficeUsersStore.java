package net.pladema.user.controller.mapper;


import net.pladema.permissions.controller.dto.BackofficeUserRoleDto;
import net.pladema.permissions.controller.mappers.UserRoleDtoMapper;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.user.controller.dto.BackofficeUserDto;
import net.pladema.user.controller.mappers.UserDtoMapper;
import net.pladema.user.repository.UserRepository;
import net.pladema.user.repository.entity.User;
import org.springframework.data.domain.*;
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


	public BackofficeUsersStore(UserRepository repository,
			UserRoleRepository userRoleRepository,
			UserDtoMapper userDtoMapper,
			UserRoleDtoMapper userRoleDtoMapper
			) {
		this.repository = repository;
		this.userRoleRepository = userRoleRepository;
		this.userDtoMapper = userDtoMapper;
		this.userRoleDtoMapper = userRoleDtoMapper;  
	}

	@Override
	public Page<BackofficeUserDto> findAll(BackofficeUserDto user, Pageable pageable) {
		User modelUser = userDtoMapper.toModel(user);
		/*modelUser.setAudit(new Audit());
		modelUser.setEnable(null);*/

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
		if (dto.getId() != null) {
			return update(dto);
		}
		return create(dto);
	}

	private BackofficeUserDto update(BackofficeUserDto dto) {
		BackofficeUserDto saved = repository.findById(dto.getId())
				.map(inDb -> userDtoMapper.toModel(dto, inDb))
				.map(repository::save)
				.map(userDtoMapper::toDto)
				.orElseThrow(() -> new NotFoundException("user-not-found", String.format("El usuario %s no existe", dto.getId())));


		//TODO: dto role ids are valid? we should validate before update
		List<UserRole> userRoles = userRoleRepository.findByUserId(dto.getId());
		userRoleRepository.deleteAll(roleToDelete(userRoles, toModel(dto.getRoles())));
		userRoleRepository.saveAll(roleToAdd(dto.getId(), toModel(dto.getRoles()), userRoles));

		return saved;
	}

	private BackofficeUserDto create(BackofficeUserDto dto) {
		User modelUser = userDtoMapper.toModel(dto);
		modelUser.setEnable(true);
		BackofficeUserDto saved = userDtoMapper.toDto(repository.save(modelUser));

		//TODO: dto role ids are valid? we should validate before update roleService.createUserRole(modelUser.getId(), ERole.PATIENT_USER);
		userRoleRepository.saveAll(roleToAdd(dto.getId(), toModel(dto.getRoles()), new ArrayList<UserRole>()));
		
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

}
