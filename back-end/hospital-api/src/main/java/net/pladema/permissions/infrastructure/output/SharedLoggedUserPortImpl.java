package net.pladema.permissions.infrastructure.output;

import ar.lamansys.sgh.shared.infrastructure.input.service.RoleInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.permissions.controller.external.LoggedUserExternalService;

import net.pladema.permissions.repository.enums.ERole;

import net.pladema.user.application.port.UserRoleStorage;

import net.pladema.user.domain.UserRoleBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SharedLoggedUserPortImpl implements SharedLoggedUserPort {

	private final LoggedUserExternalService loggedUserExternalService;

	private final UserRoleStorage userRoleStorage;

	@Override
	public boolean hasAdministrativeRole(Integer institutionId) {
		return loggedUserExternalService.hasAnyRoleInstitution(ERole.ADMINISTRATIVO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES).apply(institutionId);
	}

	@Override
	public boolean hasLocalManagerRoleOrRegionalManagerRole(Integer userId) {
		return userRoleStorage.getRolesByUser(userId)
				.stream()
				.anyMatch(ur -> ur.getRoleId() == ERole.GESTOR_DE_ACCESO_REGIONAL.getId() ||
						ur.getRoleId() == ERole.GESTOR_DE_ACCESO_LOCAL.getId());
	}

	@Override
	public boolean hasDomainManagerRole(Integer userId) {
		return userRoleStorage.getRolesByUser(userId)
				.stream()
				.anyMatch(ur -> ur.getRoleId() == ERole.GESTOR_DE_ACCESO_DE_DOMINIO.getId());
	}

	@Override
	public boolean hasManagerRole(Integer userId) {
		return userRoleStorage.getRolesByUser(userId)
				.stream()
				.anyMatch(ur -> ur.getRoleId() == ERole.GESTOR_DE_ACCESO_REGIONAL.getId() ||
						ur.getRoleId() == ERole.GESTOR_DE_ACCESO_LOCAL.getId() ||
						ur.getRoleId() == ERole.GESTOR_DE_ACCESO_DE_DOMINIO.getId() ||
						ur.getRoleId() == ERole.GESTOR_DE_ACCESO_INSTITUCIONAL.getId());
	}

	@Override
	public boolean hasInstitutionalManagerRole(Integer userId) {
		return userRoleStorage.getRolesByUser(userId)
				.stream()
				.anyMatch(ur -> ur.getRoleId() == ERole.GESTOR_DE_ACCESO_INSTITUCIONAL.getId());
	}

	@Override
	public List<Short> getLoggedUserRoleIds(Integer institutionId, Integer userId) {
		log.debug("Input parameters -> institutionId {}, userId {}", institutionId, userId);
		List<Short> result = userRoleStorage.getRolesByUser(userId).stream().filter(role -> institutionIsValid(institutionId, role)).map(UserRoleBo::getRoleId).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	private boolean institutionIsValid(Integer institutionId, UserRoleBo role) {
		return role.getInstitutionId().equals(institutionId) || role.getInstitutionId().equals(-1);
	}

	@Override
	public List<RoleInfoDto> getRoles(Integer userId) {
		return userRoleStorage.getRolesByUser(userId)
				.stream()
				.map(r -> new RoleInfoDto(r.getRoleId(), r.getInstitutionId(), r.getRoleDescription()))
				.collect(Collectors.toList());
	}
}
