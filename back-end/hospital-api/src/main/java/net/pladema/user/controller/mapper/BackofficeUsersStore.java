package net.pladema.user.controller.mapper;


import net.pladema.auditable.entity.Audit;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.user.controller.dto.BackofficeUserDto;
import net.pladema.user.controller.mappers.UserDtoMapper;
import net.pladema.user.repository.UserRepository;
import net.pladema.user.repository.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeUsersStore implements BackofficeStore<BackofficeUserDto, Integer> {
	private final UserRepository repository;
	private final UserDtoMapper userDtoMapper;


	public BackofficeUsersStore(UserRepository repository, UserDtoMapper userDtoMapper) {
		this.repository = repository;
		this.userDtoMapper = userDtoMapper;
	}

	@Override
	public Page<BackofficeUserDto> findAll(BackofficeUserDto user, Pageable pageable) {
		User modelUser = userDtoMapper.toModel(user);
		modelUser.setAudit(new Audit());
		modelUser.setEnable(null);

		return repository.findAll(
				Example.of(modelUser),
				PageRequest.of(
						pageable.getPageNumber(),
						pageable.getPageSize(),
						Sort.unsorted()
				)
		).map(userDtoMapper::toDto);
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
				.map(userDtoMapper::toDto);
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
				.map(toSave -> repository.save(toSave))
				.map(userDtoMapper::toDto)
				.orElseThrow(() -> new NotFoundException("user-not-found", String.format("El usuario %s no existe", dto.getId())));

		return saved;
	}

	private BackofficeUserDto create(BackofficeUserDto dto) {
		User modelUser = userDtoMapper.toModel(dto);
		modelUser.setEnable(true);
		BackofficeUserDto saved = userDtoMapper.toDto(repository.save(modelUser));

		return saved;
	}

	@Override
	public void deleteById(Integer id) {
		repository.changeStatusAccount(id, false);
	}

}
