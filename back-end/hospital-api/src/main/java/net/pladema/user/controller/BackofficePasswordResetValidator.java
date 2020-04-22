package net.pladema.user.controller;

import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.user.repository.entity.PasswordResetToken;

public class BackofficePasswordResetValidator
		implements BackofficePermissionValidator<PasswordResetToken, Long> {
	private final BackofficeAuthoritiesValidator authoritiesValidator;
	public BackofficePasswordResetValidator(BackofficeAuthoritiesValidator authoritiesValidator) {
		this.authoritiesValidator = authoritiesValidator;
	}

	@Override
	public void assertGetList(PasswordResetToken entity) {
		authoritiesValidator.assertAllowed(entity.getUserId());
 	}

	@Override
	public void assertGetOne(Long id) {
		throw new PermissionDeniedException("Obtener código");
	}

	@Override
	public void assertCreate(PasswordResetToken entity) {
		authoritiesValidator.assertAllowed(entity.getUserId());
	}

	@Override
	public void assertUpdate(Long id, PasswordResetToken entity) {
		throw new PermissionDeniedException("Actualizar código");
	}

	@Override
	public void assertDelete(Long id) {
		throw new PermissionDeniedException("Borrar código");
	}
}
