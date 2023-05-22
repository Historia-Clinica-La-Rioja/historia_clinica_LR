package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroup;

import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackofficeSnomedGroupValidator implements BackofficePermissionValidator<SnomedGroup, Integer> {

	public static final String NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS = "No cuenta con suficientes privilegios";

	private final PermissionEvaluator permissionEvaluator;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private final SnomedGroupRepository snomedGroupRepository;

	public BackofficeSnomedGroupValidator(PermissionEvaluator permissionEvaluator,
										  BackofficeAuthoritiesValidator authoritiesValidator,
										  SnomedGroupRepository snomedGroupRepository) {
		this.permissionEvaluator = permissionEvaluator;
		this.authoritiesValidator = authoritiesValidator;
		this.snomedGroupRepository = snomedGroupRepository;
	}

	@Override
	public void assertGetList(SnomedGroup entity) {
		// nothing to do
	}

	@Override
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return ids;
	}

	@Override
	public void assertGetOne(Integer id) {
		// nothing to do
	}

	@Override
	@PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ADMINISTRADOR')")
	public void assertCreate(SnomedGroup entity) {
		if (authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		hasPermissionByInstitution(entity.getInstitutionId());
	}

	@Override
	@PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ADMINISTRADOR')")
	public void assertUpdate(Integer id, SnomedGroup entity) {
		if (authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		SnomedGroup snomedGroup = snomedGroupRepository.findById(id).orElse(null);
		// has permission in the current institution of the group
		if (snomedGroup != null)
			hasPermissionByInstitution(snomedGroup.getInstitutionId());
		// has permission in the new institution of the group
		hasPermissionByInstitution(entity.getInstitutionId());
	}

	@Override
	@PreAuthorize("hasPermission(#entity.institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ADMINISTRADOR')")
	public void assertDelete(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		SnomedGroup snomedGroup = snomedGroupRepository.findById(id).orElse(null);
		// has permission in the institution of the group
		if (snomedGroup != null)
			hasPermissionByInstitution(snomedGroup.getInstitutionId());
	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList(SnomedGroup entity) {
		return new ItemsAllowed<>();
	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return new ItemsAllowed<>();
	}

	private void hasPermissionByInstitution(Integer institutionId) {
		if (institutionId == null)
			throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
			throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
		if (!permissionEvaluator.hasPermission(authentication, institutionId, "Institution", "ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE"))
			throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
	}


}
