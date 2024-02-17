package ar.lamansys.sgh.publicapi.userinformation.input.service;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;
import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@AllArgsConstructor
@Service
public class AppUserInformationPublicApiPermission implements UserInformationPublicApiPermission {
	private final UserSessionStorage userSessionStorage;

	@Override
	public boolean canAccess() {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_USERS, -1)
		);
	}
}
