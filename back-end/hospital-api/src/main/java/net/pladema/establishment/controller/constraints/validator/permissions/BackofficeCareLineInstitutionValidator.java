package net.pladema.establishment.controller.constraints.validator.permissions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import net.pladema.establishment.repository.CareLineInstitutionRepository;
import net.pladema.establishment.repository.entity.CareLineInstitution;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

@Component
public class BackofficeCareLineInstitutionValidator implements BackofficePermissionValidator<CareLineInstitution, Integer> {

	private final CareLineInstitutionRepository repository;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	public BackofficeCareLineInstitutionValidator(CareLineInstitutionRepository repository, BackofficeAuthoritiesValidator authoritiesValidator) {
		this.repository = repository;
		this.authoritiesValidator = authoritiesValidator;
	}

	@Override
	public void assertGetList(CareLineInstitution entity) {
	}

	@Override
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return null;
	}

	@Override
	public void assertGetOne(Integer id) {

	}

	@Override
	public void assertCreate(CareLineInstitution entity) {

	}

	@Override
	public void assertUpdate(Integer id, CareLineInstitution entity) {

	}

	@Override
	public void assertDelete(Integer id) {

	}

	@Override
	public ItemsAllowed itemsAllowedToList(CareLineInstitution entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();

		List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		if (allowedInstitutions.isEmpty())
			return new ItemsAllowed<>(false, Collections.emptyList());
		List<CareLineInstitution> entitiesByExample = repository.findAll(Example.of(entity));
		List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
		List<Integer> resultIds = entitiesByExample.stream().filter(css -> idsAllowed.contains(css.getId())).map(CareLineInstitution::getId).collect(Collectors.toList());
		return new ItemsAllowed<>(false, resultIds);
	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return null;
	}
}
