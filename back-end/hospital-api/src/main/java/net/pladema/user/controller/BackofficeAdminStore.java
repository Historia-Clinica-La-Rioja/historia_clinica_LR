package net.pladema.user.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import ar.lamansys.sgx.shared.admin.AdminConfiguration;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.user.controller.dto.AdminUserDto;
import net.pladema.user.controller.mappers.UserDtoMapper;

@Service
public class BackofficeAdminStore implements BackofficeStore<AdminUserDto, Integer> {
	private final UserRepository userRepository;

	private final UserDtoMapper userDtoMapper;

	public BackofficeAdminStore(UserRepository userRepository, UserDtoMapper userDtoMapper) {
		this.userRepository = userRepository;
		this.userDtoMapper = userDtoMapper;
	}

	@Override
	public Page<AdminUserDto> findAll(AdminUserDto user, Pageable pageable) {
		List<AdminUserDto> result = adminList();
		return new PageImpl<>(result, pageable, result.size());
	}

	@Override
	public List<AdminUserDto> findAll() {
		return adminList();
	}

	@Override
	public List<AdminUserDto> findAllById(List<Integer> ids) {
		return adminList();
	}

	private List<AdminUserDto> adminList(){
		List<AdminUserDto> result = new ArrayList<>();
		userRepository.findByUsername(AdminConfiguration.ADMIN_USERNAME_DEFAULT)
				.map(userDtoMapper::toAdminUserDto)
				.ifPresent(result::add);
		return result;
	}

	@Override
	public Optional<AdminUserDto> findById(Integer id) {
		return userRepository.findByUsername("admin@example.com")
				.map(userDtoMapper::toAdminUserDto);
	}

	@Override
	public AdminUserDto save(AdminUserDto dto) {
		return dto;
	}

	@Override
	public void deleteById(Integer id) {
	}

	@Override
	public Example<AdminUserDto> buildExample(AdminUserDto entity) {
		return Example.of(entity);
	}
}
