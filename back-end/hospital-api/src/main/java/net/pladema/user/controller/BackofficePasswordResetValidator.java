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
		if (authoritiesValidator.isLoggedUserId(entity.getUserId())) {
			// se puede cambiar el password a si mismo
			return;
		}
		authoritiesValidator.assertLoggedUserOutrank(entity.getUserId());
 	}

	@Override
	public void assertGetOne(Long id) {
		throw new PermissionDeniedException("Obtener código");
	}

	@Override
	public void assertCreate(PasswordResetToken entity) {
		if (authoritiesValidator.isLoggedUserId(entity.getUserId())) {
			// se puede cambiar el password a si mismo
			return;
		}
		authoritiesValidator.assertLoggedUserOutrank(entity.getUserId());
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
