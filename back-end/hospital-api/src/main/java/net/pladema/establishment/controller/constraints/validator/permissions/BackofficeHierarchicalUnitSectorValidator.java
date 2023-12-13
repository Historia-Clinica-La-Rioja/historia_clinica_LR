package net.pladema.establishment.controller.constraints.validator.permissions;

import net.pladema.establishment.repository.HierarchicalUnitSectorRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitSector;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackofficeHierarchicalUnitSectorValidator implements BackofficePermissionValidator<HierarchicalUnitSector, Integer> {

	private final HierarchicalUnitSectorRepository repository;

	public BackofficeHierarchicalUnitSectorValidator(HierarchicalUnitSectorRepository hierarchicalUnitSectorRepository){
		this.repository = hierarchicalUnitSectorRepository;
	}
	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetList(HierarchicalUnitSector entity) {

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
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertCreate(HierarchicalUnitSector entity) {
		assertNotExists(entity);
		assertBelongToSameInstitution(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertUpdate(Integer id, HierarchicalUnitSector entity) {
		assertNotExists(entity);
		assertBelongToSameInstitution(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertDelete(Integer id) {
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed<Integer> itemsAllowedToList(HierarchicalUnitSector entity) {
		return new ItemsAllowed<>();
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return new ItemsAllowed<>();
	}

	private void assertNotExists(HierarchicalUnitSector entity){
		if(repository.existsByHierarchicalUnitAndSector(entity.getHierarchicalUnitId(), entity.getSectorId()))
			throw new BackofficeValidationException("hierarchical-unit.sector.exists");
	}

	private void assertBelongToSameInstitution(HierarchicalUnitSector entity){
		if(!repository.belongToSameInstitution(entity.getHierarchicalUnitId(), entity.getSectorId()))
			throw new BackofficeValidationException("hierarchical-unit.sector.different.institution");
	}

}
