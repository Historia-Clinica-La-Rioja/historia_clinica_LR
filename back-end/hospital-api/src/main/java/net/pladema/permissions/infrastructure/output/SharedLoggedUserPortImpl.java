package net.pladema.permissions.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import lombok.RequiredArgsConstructor;
import net.pladema.permissions.controller.external.LoggedUserExternalService;

import net.pladema.permissions.repository.enums.ERole;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SharedLoggedUserPortImpl implements SharedLoggedUserPort {

	private final LoggedUserExternalService loggedUserExternalService;

	@Override
	public boolean hasAdministrativeRole(Integer institutionId) {
		return loggedUserExternalService.hasAnyRoleInstitution(ERole.ADMINISTRATIVO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES).apply(institutionId);
	}
}
