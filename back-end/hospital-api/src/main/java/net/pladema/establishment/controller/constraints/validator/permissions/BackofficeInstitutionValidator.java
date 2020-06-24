package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.entity.Institution;
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
public class BackofficeInstitutionValidator implements BackofficePermissionValidator<Institution, Integer> {

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private final PermissionEvaluator permissionEvaluator;

	public BackofficeInstitutionValidator(BackofficeAuthoritiesValidator backofficeAuthoritiesValidator,
                                          PermissionEvaluator permissionEvaluator) {
		this.authoritiesValidator = backofficeAuthoritiesValidator;
		this.permissionEvaluator = permissionEvaluator;
	}


	@Override
	@PreAuthorize("hasPermission(#entity.id, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertGetList(Institution entity) {
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
		hasPermissionByInstitution(id);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertCreate(Institution entity) {
		// nothing to do
	}

	@Override
	@PreAuthorize("hasPermission(#id, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertUpdate(Integer id, Institution entity) {
		// nothing to do
	}

	@Override
	public void assertDelete(Integer id) {
		hasPermissionByInstitution(id);
	}

	private void hasPermissionByInstitution(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = id;
		if (institutionId == null)
			return;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
		if (!permissionEvaluator.hasPermission(authentication, institutionId, "ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE"))
			throw new PermissionDeniedException("No cuenta con suficientes privilegios");
	}

}
