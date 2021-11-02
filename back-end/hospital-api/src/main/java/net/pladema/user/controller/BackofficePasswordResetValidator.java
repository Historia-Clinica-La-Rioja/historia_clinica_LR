package net.pladema.user.controller;

import ar.lamansys.sgx.auth.user.infrastructure.output.userpassword.PasswordResetToken;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.PermissionDeniedException;

import java.util.ArrayList;
import java.util.List;

public class BackofficePasswordResetValidator
		implements BackofficePermissionValidator<PasswordResetToken, Long> {
	private final BackofficeAuthoritiesValidator authoritiesValidator;
	public BackofficePasswordResetValidator(BackofficeAuthoritiesValidator authoritiesValidator) {
		this.authoritiesValidator = authoritiesValidator;
	}

	@Override
	public void assertGetList(PasswordResetToken entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT)) {
			throw new PermissionDeniedException("No se puede resetear la contraseña al admin");
		}

		if (authoritiesValidator.isLoggedUserId(entity.getUserId())) {
			// se puede cambiar el password a si mismo
			return;
		}
		authoritiesValidator.assertLoggedUserOutrank(entity.getUserId());
	}

	@Override
	public List<Long> filterIdsByPermission(List<Long> ids) {
		return ids;
	}

	@Override
	public void assertGetOne(Long id) {
		throw new PermissionDeniedException("Obtener código");
	}

	@Override
	public void assertCreate(PasswordResetToken entity) {
		if (authoritiesValidator.isLoggedUserId(entity.getUserId()))
			return;
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

	@Override
	public ItemsAllowed itemsAllowedToList(PasswordResetToken entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>(true, new ArrayList<>());
		return new ItemsAllowed<>(false, new ArrayList<>());
	}

	@Override
	public ItemsAllowed itemsAllowedToList() {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>(true, new ArrayList<>());
		return new ItemsAllowed<>(false, new ArrayList<>());
	}
}
