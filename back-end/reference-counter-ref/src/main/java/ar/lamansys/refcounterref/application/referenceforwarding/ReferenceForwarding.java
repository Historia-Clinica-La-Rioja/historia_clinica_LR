package ar.lamansys.refcounterref.application.referenceforwarding;

import ar.lamansys.refcounterref.application.port.ReferenceForwardingStorage;
import ar.lamansys.refcounterref.domain.enums.EReferenceForwardingType;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.sgh.shared.infrastructure.input.service.RoleInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ReferenceForwarding {

	private final static Short GESTOR_DE_ACCESO_LOCAL_ROL_ID = 39;

	private final static Short GESTOR_DE_ACCESO_REGIONAL_ROL_ID = 40;

	private final ReferenceForwardingStorage referenceForwardingStorage;

	private final SharedLoggedUserPort sharedLoggedUserPort;

	public void run(Integer referenceId, String observation) {
		log.debug("Input parameters -> referenceId {}, observation {}", referenceId, observation);
		var userRoles = sharedLoggedUserPort.getRoles(UserInfo.getCurrentAuditor());
		referenceForwardingStorage.save(referenceId, observation, getForwardingType(userRoles));
 	}

	private Short getForwardingType(List<RoleInfoDto> userRoles) {
		return userRoles.stream().anyMatch(ur -> ur.getId().equals(GESTOR_DE_ACCESO_LOCAL_ROL_ID))
				? EReferenceForwardingType.REGIONAL.getId()
				: userRoles.stream().anyMatch(ur -> ur.getId().equals(GESTOR_DE_ACCESO_REGIONAL_ROL_ID))
				? EReferenceForwardingType.DOMAIN.getId()
				: -1;
	}

}
