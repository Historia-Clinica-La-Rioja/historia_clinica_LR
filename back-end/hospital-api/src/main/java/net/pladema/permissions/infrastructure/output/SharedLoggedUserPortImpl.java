package net.pladema.permissions.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import lombok.RequiredArgsConstructor;
import net.pladema.permissions.controller.external.LoggedUserExternalService;

import net.pladema.permissions.repository.enums.ERole;

import net.pladema.user.application.port.UserRoleStorage;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SharedLoggedUserPortImpl implements SharedLoggedUserPort {

	private final LoggedUserExternalService loggedUserExternalService;

	private final UserRoleStorage userRoleStorage;

	@Override
	public boolean hasAdministrativeRole(Integer institutionId) {
		return loggedUserExternalService.hasAnyRoleInstitution(ERole.ADMINISTRATIVO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES).apply(institutionId);
	}

	@Override
	public boolean hasLocalManagerRoleOrRegionalManagerRole(Integer userId) {
		return userRoleStorage.getRolesByUser(userId)
				.stream()
				.anyMatch(ur -> ur.getRoleId() == ERole.GESTOR_DE_ACCESO_REGIONAL.getId() ||
						ur.getRoleId() == ERole.GESTOR_DE_ACCESO_LOCAL.getId());
	}
}
