package net.pladema.establishment.controller.constraints.validator.permissions;

import lombok.AllArgsConstructor;
import net.pladema.establishment.repository.CareLineRoleRepository;
import net.pladema.establishment.repository.entity.CareLineRole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class BackofficeCareLineRoleValidator implements BackofficePermissionValidator<CareLineRole, Integer> {

	private CareLineRoleRepository careLineRoleRepository;

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertGetList(CareLineRole entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return null;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertGetOne(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertCreate(CareLineRole entity) {
		Boolean roleAlreadyAssigned = careLineRoleRepository.alreadyExistsCareLineAssignedRole(entity.getCareLineId(), entity.getRoleId());
		if (roleAlreadyAssigned)
			throw new BackofficeValidationException("El rol ya se encuentra asignado a ésta línea de cuidado");
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertUpdate(Integer id, CareLineRole entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertDelete(Integer id) {

	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList(CareLineRole entity) {
		return new ItemsAllowed<>();
	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return null;
	}

}
