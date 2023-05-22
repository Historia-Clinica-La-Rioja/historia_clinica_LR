package net.pladema.person.controller.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.session.application.port.UserSessionStorage;
import net.pladema.user.application.getrolesbyuser.GetRolesByUser;
import net.pladema.user.application.port.HospitalUserStorage;

@Service
@Slf4j
@RequiredArgsConstructor
public class CanEditUserData {

	private final HospitalUserStorage hospitalUserStorage;

	private final GetRolesByUser getRolesByUser;

	private final UserSessionStorage userSessionStorage;

	public Boolean run(Integer personId, Integer institutionId) {
		log.debug("Input parameters -> personId {}, institutionId {}", personId, institutionId);
		boolean result = hospitalUserStorage.getUserDataByPersonId(personId).map(userData -> {
			boolean hasRolToEditUserData = userSessionStorage.getRolesAssigned().anyMatch(role -> role.isAssigment(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, institutionId) || role.isAssigment(ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, institutionId));
			if (!hasRolToEditUserData) return false;
			return !getRolesByUser.execute(userData.getId(), institutionId).isEmpty();
		}).orElse(true);
		log.debug("Output result -> {}", result);
		return result;
	}
}
