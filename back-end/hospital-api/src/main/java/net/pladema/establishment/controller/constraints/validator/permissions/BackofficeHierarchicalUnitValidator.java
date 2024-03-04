package net.pladema.establishment.controller.constraints.validator.permissions;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.HierarchicalUnitRelationshipRepository;
import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static net.pladema.establishment.repository.entity.HierarchicalUnitType.SERVICIO;

@Component
@RequiredArgsConstructor
public class BackofficeHierarchicalUnitValidator implements BackofficePermissionValidator<HierarchicalUnit, Integer> {

	private final HierarchicalUnitRepository repository;

	private final HierarchicalUnitRelationshipRepository hierarchicalUnitRelationshipRepository;

    @Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetList(HierarchicalUnit entity) {
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return ids;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetOne(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertCreate(HierarchicalUnit entity) {
		if (validateHierarchicalUnitExistsByAliasInInstitutionID(entity.getAlias(), entity.getInstitutionId()))
			throw new BackofficeValidationException("hierarchical-unit.alias.exists");
		validateClinicalSpecialtyIdData(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertUpdate(Integer id, HierarchicalUnit entity) {
		repository.findById(id).ifPresentOrElse(hu -> {
			if (!hu.getAlias().equals(entity.getAlias()) && validateHierarchicalUnitExistsByAliasInInstitutionID(entity.getAlias(), entity.getInstitutionId()))
				throw new BackofficeValidationException("hierarchical-unit.alias.exists");
			}, () -> new BackofficeValidationException("hierarchical-unit.invalid-id")
		);
		validateClinicalSpecialtyIdData(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertDelete(Integer id) {
		if (hierarchicalUnitRelationshipRepository.existsParentRelationship(id))
			throw new BackofficeValidationException("hierarchical-unit.affected");
	}

	@Override
	public ItemsAllowed itemsAllowedToList(HierarchicalUnit entity) {
		return new ItemsAllowed<>();
	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return new ItemsAllowed<>();
	}

	private void validateClinicalSpecialtyIdData(HierarchicalUnit entity) {
		if (entity.getClinicalSpecialtyId() != null && !entity.getTypeId().equals((int)SERVICIO))
			entity.setClinicalSpecialtyId(null);
	}
	private boolean validateHierarchicalUnitExistsByAliasInInstitutionID(String alias, Integer institutionId) {
		return repository.existsByAliasAndInstitutionId(alias, institutionId);
	}
}