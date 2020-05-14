package net.pladema.user.controller;


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
	public void assertUpdate(Integer id, BackofficeUserDto entity) {
		if (authoritiesValidator.isLoggedUserId(id)) {
			throw new PermissionDeniedException("No te podés editar a vos mismo");
		}
		authoritiesValidator.assertAllowed(id);
	}

	@Override
	public void assertDelete(Integer id) {
		if (authoritiesValidator.isLoggedUserId(id)) {
			throw new PermissionDeniedException("Operación no permitida");
		}
		authoritiesValidator.assertAllowed(id);
	}
}
