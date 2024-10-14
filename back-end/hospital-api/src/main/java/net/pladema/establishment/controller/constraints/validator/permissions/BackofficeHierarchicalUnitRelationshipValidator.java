package net.pladema.establishment.controller.constraints.validator.permissions;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.HierarchicalUnitRelationshipRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitRelationship;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BackofficeHierarchicalUnitRelationshipValidator implements BackofficePermissionValidator<HierarchicalUnitRelationship, Integer> {

	private final HierarchicalUnitRelationshipRepository repository;

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetList(HierarchicalUnitRelationship entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return null;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetOne(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertCreate(HierarchicalUnitRelationship entity) {
		validateRelationship(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertUpdate(Integer id, HierarchicalUnitRelationship entity) {
		validateRelationship(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertDelete(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed itemsAllowedToList(HierarchicalUnitRelationship entity) {
		return new ItemsAllowed<>();
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed itemsAllowedToList() {
		return new ItemsAllowed<>();
	}

	private void validateRelationship(HierarchicalUnitRelationship entity) {
		if (entity.getHierarchicalUnitChildId() == null || entity.getHierarchicalUnitParentId() == null)
			throw new BackofficeValidationException("hierarchical-unit-relationship.null.fields");
		if (entity.getHierarchicalUnitChildId().equals(entity.getHierarchicalUnitParentId()))
			throw new BackofficeValidationException("hierarchical-unit-relationship.parentOfItself");
		if (repository.existsRelationship(entity.getHierarchicalUnitChildId(), entity.getHierarchicalUnitParentId()))
			throw new BackofficeValidationException("hierarchical-unit-relationship.exists");
	}
}
