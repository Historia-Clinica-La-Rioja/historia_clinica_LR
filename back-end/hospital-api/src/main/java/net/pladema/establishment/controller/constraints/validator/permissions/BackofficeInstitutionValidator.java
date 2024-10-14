package net.pladema.establishment.controller.constraints.validator.permissions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.sgx.session.application.port.UserSessionStorage;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

@Component
public class BackofficeInstitutionValidator implements BackofficePermissionValidator<Institution, Integer> {

	public static final String NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS = "No cuenta con suficientes privilegios";

	public static final String SISA_CODE_ALREADY_USED = "El código SISA ingresado ya se encuentra asignado a otra institución";

	private final InstitutionRepository repository;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private final PermissionEvaluator permissionEvaluator;

	private final Supplier<Boolean> userCanView;

	public BackofficeInstitutionValidator(
			InstitutionRepository repository,
			BackofficeAuthoritiesValidator backofficeAuthoritiesValidator,
			UserSessionStorage userSessionStorage,
			PermissionEvaluator permissionEvaluator
	) {
		this.repository = repository;
		this.authoritiesValidator = backofficeAuthoritiesValidator;
		this.permissionEvaluator = permissionEvaluator;
		this.userCanView = userSessionStorage.hasAnyRole(
				// quien puede ver las instituciones
				ERole.ROOT,
				ERole.ADMINISTRADOR,
				ERole.API_IMAGENES,
				ERole.ADMINISTRADOR_DE_ACCESO_DOMINIO
		);
	}

	@Override
	@PreAuthorize("hasPermission(#entity.id, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'API_IMAGENES')")
	public void assertGetList(Institution entity) {
		// nothing to do
	}

	@Override
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		if (userCanView.get())
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
	@PreAuthorize("hasPermission(#id, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'API_IMAGENES')")
	public void assertGetOne(Integer id) {
		// Do nothing
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertCreate(Institution entity) {
		verifyUniqueSISACode(entity);
	}

	private void verifyUniqueSISACode(Institution entity) {
		boolean SISACodeAlreadyUsed = !repository.findIdsBySisaCode(entity.getSisaCode()).isEmpty();
		if (SISACodeAlreadyUsed)
			throw new PermissionDeniedException(SISA_CODE_ALREADY_USED);
	}

	@Override
	@PreAuthorize("hasPermission(#id, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE') || hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertUpdate(Integer id, Institution entity) {
		verifyUniqueSISACode(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertDelete(Integer id) {
		// nothing
	}

	@Override
	public ItemsAllowed itemsAllowedToList(Institution entity) {
		if (userCanView.get())
			return new ItemsAllowed<>();
		List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		if (allowedInstitutions.isEmpty())
			return new ItemsAllowed<>(false, Collections.emptyList());
		return new ItemsAllowed<>(false, allowedInstitutions);
	}

	@Override
	public ItemsAllowed itemsAllowedToList() {
		if (userCanView.get())
			return new ItemsAllowed<>();
		List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		if (allowedInstitutions.isEmpty())
			return new ItemsAllowed<>(false, Collections.emptyList());
		return new ItemsAllowed<>(false, allowedInstitutions);
	}

	private void hasPermissionByInstitution(Integer institutionId) {
		if (institutionId == null)
			throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser"))
			throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
		if (!permissionEvaluator.hasPermission(authentication, institutionId, "ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE"))
			throw new PermissionDeniedException(NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS);
	}

	public Example<Institution> buildExample(Institution entity) {
		ExampleMatcher matcher = ExampleMatcher
				.matching()
				.withMatcher("name", x -> x.ignoreCase().contains())
				.withMatcher("sisaCode", x -> x.ignoreCase().contains());
		return Example.of(entity, matcher);
	}
}
