package ar.lamansys.sgh.publicapi.activities.infrastructure.input.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.ApiConsumerCondition;
import lombok.AllArgsConstructor;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;

@AllArgsConstructor
@Service
public class AppActivitiesPublicApiPermissions implements ActivitiesPublicApiPermissions {

	private final UserSessionStorage userSessionStorage;
	private final ApiConsumerCondition apiConsumerCondition;

	private final InstitutionRepository institutionRepository;

	@Override
	public boolean canAccess(Integer institutionId) {
		return userSessionStorage.getRolesAssigned().anyMatch(
				roleAssigment -> roleAssigment.isAssigment(ERole.API_FACTURACION, institutionId)
						|| apiConsumerCondition.isRole(roleAssigment)
		);
	}

	@Override
	public Optional<Integer> findInstitutionId(String refsetCode) {
		return institutionRepository.findIdBySisaCode(refsetCode);
	}


}