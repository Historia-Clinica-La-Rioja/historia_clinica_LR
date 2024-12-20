package ar.lamansys.sgh.publicapi.userinformation.input.service;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.ApiConsumerCondition;
import ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service.UserInformationPublicApiPermission;
import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@AllArgsConstructor
@Service
public class AppUserInformationPublicApiPermission implements UserInformationPublicApiPermission {
	private final UserSessionStorage userSessionStorage;
	private final ApiConsumerCondition apiConsumerCondition;

	@Override
	public boolean canAccess() {
		return checkApiUsers();
	}

	@Override
	public boolean canAccessUserInfoFromToken() {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_USERS, -1)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canAccessUserAuthoritiesFromToken() {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_USERS, -1)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public boolean canFetchProfessionalLicenses() {
		return checkApiUsers();
	}

	private boolean checkApiUsers() {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_USERS, -1)
		);
	}
}
