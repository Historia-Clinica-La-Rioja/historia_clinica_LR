package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.ClinicalServiceSectorRepository;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.data.domain.Example;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BackofficeClinicalServiceSectorValidator implements BackofficePermissionValidator<ClinicalSpecialtySector, Integer> {

	public static final String NO_CUENTA_CON_SUFICIENTES_PRIVILEGIOS = "No cuenta con suficientes privilegios";
	private final ClinicalServiceSectorRepository repository;

	private final SectorRepository sectorRepository;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private final PermissionEvaluator permissionEvaluator;

	public BackofficeClinicalServiceSectorValidator(ClinicalServiceSectorRepository repository,
													  SectorRepository sectorRepository,
													  BackofficeAuthoritiesValidator backofficeAuthoritiesValidator,
													  PermissionEvaluator permissionEvaluator) {
		this.repository = repository;
		this.sectorRepository = sectorRepository;
		this.authoritiesValidator = backofficeAuthoritiesValidator;
		this.permissionEvaluator = permissionEvaluator;
	}


	@Override
	public void assertGetList(ClinicalSpecialtySector entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = repository.getInstitutionId(entity.getId());
		hasPermissionByInstitution(institutionId);
	}

	@Override
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return ids;
		return ids.stream().filter(id -> {
			try {
				Integer institutionId = repository.getInstitutionId(id);
				hasPermissionByInstitution(institutionId);
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
		Integer institutionId = repository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
	}

	@Override
	public void assertCreate(ClinicalSpecialtySector entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = sectorRepository.getInstitutionId(entity.getSectorId());
		hasPermissionByInstitution(institutionId);
	}

	@Override
	public void assertUpdate(Integer id, ClinicalSpecialtySector entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = repository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
	}

	@Override
	public void assertDelete(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
		Integer institutionId = repository.getInstitutionId(id);
		hasPermissionByInstitution(institutionId);
	}

	@Override
	public ItemsAllowed itemsAllowedToList(ClinicalSpecialtySector entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();

		List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		if (allowedInstitutions.isEmpty())
			return new ItemsAllowed<>(false, Collections.emptyList());
		List<ClinicalSpecialtySector> entitiesByExample = repository.findAll(Example.of(entity));
		List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
		List<Integer> resultIds = entitiesByExample.stream().filter(css -> idsAllowed.contains(css.getId())).map(ClinicalSpecialtySector::getId).collect(Collectors.toList());
		return new ItemsAllowed<>(false, resultIds);
	}

	@Override
	public ItemsAllowed itemsAllowedToList() {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();
		List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		if (allowedInstitutions.isEmpty())
			return new ItemsAllowed<>(false, Collections.emptyList());
		List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
		return new ItemsAllowed<>(false, idsAllowed);
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

}
