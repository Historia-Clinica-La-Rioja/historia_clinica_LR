package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BackofficeSectorValidator implements BackofficePermissionValidator<Sector, Integer> {

	private final SectorRepository sectorRepository;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private final PermissionEvaluator permissionEvaluator;

	public BackofficeSectorValidator(SectorRepository sectorRepository,
									 BackofficeAuthoritiesValidator backofficeAuthoritiesValidator,
									 PermissionEvaluator permissionEvaluator) {
		this.sectorRepository = sectorRepository;
		this.authoritiesValidator = backofficeAuthoritiesValidator;
		this.permissionEvaluator = permissionEvaluator;
	}


	@Override
	@PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertGetList(Sector entity) {
		// nothing to do
	}

	@Override
	public List<Integer> filterByPermission(List<Integer> ids) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return ids;
		return ids.stream().filter(id -> {
			try {
				hasPermissionByInstitution(id);
				return true;
			} catch (Exception e) {
				return false;
			}
		}).collect(Collectors.toList());
	}

	@Override
	public void assertGetOne(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = sectorRepository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
	}

	@Override
	@PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertCreate(Sector entity) {
		// nothing to do
	}

	@Override
	@PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertUpdate(Integer id, Sector entity) {
		// nothing to do
	}

	@Override
	public void assertDelete(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = sectorRepository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
	}

	private void hasPermissionByInstitution(Integer institutionId) {
		if (institutionId == null)
			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
		if (!permissionEvaluator.hasPermission(authentication, institutionId, "ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE"))
			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
	}
}
