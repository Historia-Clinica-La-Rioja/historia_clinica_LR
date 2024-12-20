package ar.lamansys.refcounterref.application.referenceforwarding;

import ar.lamansys.refcounterref.application.referenceforwarding.exceptions.ReferenceForwardingException;
import ar.lamansys.refcounterref.application.referenceforwarding.exceptions.ReferenceForwardingExceptionEnum;
import ar.lamansys.refcounterref.application.port.ReferenceForwardingStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
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

	private final ReferenceStorage referenceStorage;

	private final ReferenceForwardingStorage referenceForwardingStorage;

	private final SharedLoggedUserPort sharedLoggedUserPort;

	public void run(Integer referenceId, String observation) {
		log.debug("Input parameters -> referenceId {}, observation {}", referenceId, observation);
		var userRoles = sharedLoggedUserPort.getRoles(UserInfo.getCurrentAuditor());
		var forwardingTypeId = getForwardingType(userRoles);
		assertContextValid(referenceId, observation, userRoles, forwardingTypeId);
		referenceForwardingStorage.save(referenceId, observation, forwardingTypeId);
 	}

	 private void assertContextValid(Integer referenceId, String observation, List<RoleInfoDto> roles, Short forwardingTypeId) {
		var regulationStateId = referenceStorage.getReferenceRegulationStateId(referenceId);
		if (!hasLocalManagerRoleOrRegionalManagerRole(roles))
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.INVALID_ROLE, "No posee un rol que pueda derivar una solicitud de referencia");
		if (regulationStateId.equals(EReferenceRegulationState.REJECTED.getId()))
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.INVALID_REFERENCE_STATE, "Para derivar una solicitud el estado de la regulación debe ser Aprobada o Esperando aprobación");
		if (referenceId == null)
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.NULL_REFRENCE_ID, "El id de la referencia es obligatorio");
		if (observation == null || observation.isBlank())
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.NULL_OBSERVATION, "El texto de observación es obligatorio");
		if (forwardingTypeId.equals(EReferenceForwardingType.REGIONAL.getId()) && referenceForwardingStorage.hasRegionalForwarding(referenceId))
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.REGIONAL_FORWARDING_ALREADY_EXISTS, "La referencia puede ser derivada a un gestor regional una unica vez");
		if (forwardingTypeId.equals(EReferenceForwardingType.DOMAIN.getId()) && referenceForwardingStorage.hasDomainForwarding(referenceId))
			throw new ReferenceForwardingException(ReferenceForwardingExceptionEnum.DOMAIN_FORWARDING_ALREADY_EXISTS, "La referencia puede ser derivada a un gestor de dominio una unica vez");
	 }

	 private boolean hasLocalManagerRoleOrRegionalManagerRole(List<RoleInfoDto> roles) {
		 return roles.stream().anyMatch(ur -> ur.getId().equals(GESTOR_DE_ACCESO_REGIONAL_ROL_ID)|| ur.getId().equals(GESTOR_DE_ACCESO_LOCAL_ROL_ID));
	 }

	private Short getForwardingType(List<RoleInfoDto> userRoles) {
		return userRoles.stream().anyMatch(ur -> ur.getId().equals(GESTOR_DE_ACCESO_LOCAL_ROL_ID))
				? EReferenceForwardingType.REGIONAL.getId()
				: userRoles.stream().anyMatch(ur -> ur.getId().equals(GESTOR_DE_ACCESO_REGIONAL_ROL_ID))
				? EReferenceForwardingType.DOMAIN.getId()
				: -1;
	}

}
