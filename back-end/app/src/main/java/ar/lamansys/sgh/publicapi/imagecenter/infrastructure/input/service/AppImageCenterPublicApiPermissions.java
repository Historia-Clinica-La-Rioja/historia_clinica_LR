package ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.service;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.ApiConsumerCondition;
import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@AllArgsConstructor
@Service
public class AppImageCenterPublicApiPermissions implements ImageCenterPublicApiPermissions {

	private final UserSessionStorage userSessionStorage;
	private final ApiConsumerCondition apiConsumerCondition;

	@Override
	public boolean canUpdate(Integer institutionId) {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_ORQUESTADOR, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}
}
