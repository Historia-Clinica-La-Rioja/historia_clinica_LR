package ar.lamansys.sgh.publicapi;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.dto.RoleAssignment;

@Service
public class ApiConsumerCondition {

	private final Function<RoleAssignment, Boolean> assertHasApiConsumer;

	public ApiConsumerCondition(
			FeatureFlagsService featureFlagsService
	) {
		// Se mantiene este FF en true por retrocompatibilidad, La idea es eliminar este flag
		this.assertHasApiConsumer = (featureFlagsService.isOn(AppFeature.HABILITAR_API_CONSUMER)) ?
				roleAssignment -> roleAssignment.isAssigment(ERole.API_CONSUMER, -1) :
				roleAssignment -> false; // API_CONSUMER no debería habilitar el acceso a TODA el API Pública
	}

	public boolean isRole(RoleAssignment roleAssignment) {
		return assertHasApiConsumer.apply(roleAssignment);
	}

}
