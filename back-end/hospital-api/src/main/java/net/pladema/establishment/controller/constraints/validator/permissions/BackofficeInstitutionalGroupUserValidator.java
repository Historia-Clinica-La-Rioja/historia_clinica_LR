package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.InstitutionalGroupUserRepository;
import net.pladema.establishment.repository.entity.InstitutionalGroupUser;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import net.pladema.sgx.exceptions.BackofficeValidationException;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BackofficeInstitutionalGroupUserValidator implements BackofficeEntityValidator<InstitutionalGroupUser, Integer> {

	private final InstitutionalGroupUserRepository repository;
	private final UserRoleRepository userRoleRepository;

	public BackofficeInstitutionalGroupUserValidator(InstitutionalGroupUserRepository repository, UserRoleRepository userRoleRepository){
		this.repository = repository;
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertCreate(InstitutionalGroupUser entity) {
		if (entity.getUserId() == null || entity.getInstitutionalGroupId() == null)
			throw new BackofficeValidationException("");
		List<Short> roles = userRoleRepository.findByUserId(entity.getUserId()).stream().map(UserRole::getRoleId).collect(Collectors.toList());
		if (!roles.contains(ERole.GESTOR_DE_ACCESO_REGIONAL.getId()) &&	 !roles.contains(ERole.GESTOR_DE_ACCESO_LOCAL.getId())) {
			throw new BackofficeValidationException("institutional-group-user.invalid.user.roles");
		}
		if(roles.contains(ERole.GESTOR_DE_ACCESO_LOCAL.getId()) && repository.existsByUserId(entity.getUserId())){
			throw new BackofficeValidationException("institutional-group-user.one.group.constraint");
		}
		if(roles.contains(ERole.GESTOR_DE_ACCESO_REGIONAL.getId()) && repository.existsByInstitutionalGroupIdAndUserId(entity.getInstitutionalGroupId(), entity.getUserId())){
			throw new BackofficeValidationException("institutional-group-user.already.exists");
		}
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertUpdate(Integer id, InstitutionalGroupUser entity) {}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertDelete(Integer id) {}
}
