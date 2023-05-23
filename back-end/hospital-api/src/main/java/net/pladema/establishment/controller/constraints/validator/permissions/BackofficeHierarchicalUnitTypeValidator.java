package net.pladema.establishment.controller.constraints.validator.permissions;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.repository.HierarchicalUnitTypeRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitType;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BackofficeHierarchicalUnitTypeValidator implements BackofficePermissionValidator<HierarchicalUnitType, Integer> {
	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetList(HierarchicalUnitType entity) {

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
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
	public void assertCreate(HierarchicalUnitType entity) {
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
	public void assertUpdate(Integer id, HierarchicalUnitType entity) {
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ADMINISTRADOR')")
	public void assertDelete(Integer id) {
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed<Integer> itemsAllowedToList(HierarchicalUnitType entity) {
		return new ItemsAllowed<>();
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return new ItemsAllowed<>();
	}

}
