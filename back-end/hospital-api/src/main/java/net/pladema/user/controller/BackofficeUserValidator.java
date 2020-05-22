package net.pladema.user.controller;


import java.util.stream.Collectors;

import net.pladema.permissions.controller.dto.BackofficeUserRoleDto;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.user.controller.dto.BackofficeUserDto;

public class BackofficeUserValidator
		implements BackofficePermissionValidator<BackofficeUserDto, Integer> {
	private final BackofficeAuthoritiesValidator authoritiesValidator;

	public BackofficeUserValidator(BackofficeAuthoritiesValidator authoritiesValidator) {
		this.authoritiesValidator = authoritiesValidator;
	}

	@Override
	public void assertGetList(BackofficeUserDto entity) {
		// nothing to do
 	}

	@Override
	public void assertGetOne(Integer id) {
		// nothing to do
	}

	@Override
	public void assertCreate(BackofficeUserDto entity) {
		// nothing to do
	}

	@Override
	public void assertUpdate(Integer userId, BackofficeUserDto entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT)) {
			// ROOT puede modificar cualquier usuario
			return;
		}
		if (authoritiesValidator.isLoggedUserId(userId)) {
			throw new PermissionDeniedException("No te podés editar a vos mismo");
		}
		authoritiesValidator.assertLoggedUserOutrank(userId);

		authoritiesValidator.assertLoggedUserOutrank(
				entity.getRoles().stream()
						.map(BackofficeUserRoleDto::getRoleId)
						.map(ERole::map)
						.map(ERole::getValue)
						.collect(Collectors.toList())
		);
	}

	@Override
	public void assertDelete(Integer userId) {
		if (authoritiesValidator.isLoggedUserId(userId)) {
			throw new PermissionDeniedException("Operación no permitida");
		}
		authoritiesValidator.assertLoggedUserOutrank(userId);
	}
}
