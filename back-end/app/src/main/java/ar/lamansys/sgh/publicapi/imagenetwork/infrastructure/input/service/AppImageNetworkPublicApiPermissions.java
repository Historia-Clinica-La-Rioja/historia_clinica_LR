package ar.lamansys.sgh.publicapi.imagenetwork.infrastructure.input.service;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.ApiConsumerCondition;
import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@AllArgsConstructor
@Service
public class AppImageNetworkPublicApiPermissions implements ImageNetworkPublicApiPermissions {
	private final UserSessionStorage userSessionStorage;
	private final ApiConsumerCondition apiConsumerCondition;

	@Override
	public boolean canAccess() {
		return userSessionStorage.getRolesAssigned().anyMatch(
				role -> role.isAssigment(ERole.API_IMAGENES, -1)
					|| apiConsumerCondition.isRole(role)
		);
	}
}
