package net.pladema.establishment.controller.constraints.validator.permissions;

import lombok.RequiredArgsConstructor;

import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.HierarchicalUnitStaffRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitStaff;
import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BackofficeHierarchicalUnitStaffValidator implements BackofficePermissionValidator<HierarchicalUnitStaff, Integer> {

	private final HierarchicalUnitStaffRepository repository;

	private final HierarchicalUnitRepository hierarchicalUnitRepository;

	private final UserRoleRepository userRoleRepository;

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE', 'ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertGetList(HierarchicalUnitStaff entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE', 'ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return null;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE', 'ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public void assertGetOne(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertCreate(HierarchicalUnitStaff entity) {
		HierarchicalUnitStaff hus = repository.findByHierarchicalUnitIdAndUserId(entity.getHierarchicalUnitId(), entity.getUserId()).orElse(null);
		if (hus != null)
			throw new BackofficeValidationException("hierarchical-unit-staff.exists");

		Integer institutionId = hierarchicalUnitRepository.getById(entity.getHierarchicalUnitId()).getInstitutionId();
		if (userRoleRepository.findByInstitutionIdAndUserId(institutionId, entity.getUserId()).isEmpty())
			throw new BackofficeValidationException("hierarchical-unit-staff.institution-constraint");
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertUpdate(Integer id, HierarchicalUnitStaff entity) {
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertDelete(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE', 'ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public ItemsAllowed<Integer> itemsAllowedToList(HierarchicalUnitStaff entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();

		List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		if (allowedInstitutions.isEmpty())
			return new ItemsAllowed<>(false, Collections.emptyList());

		List<Integer> idsAllowed = repository.getAllIdsByInstitutionsId(allowedInstitutions);
		return new ItemsAllowed<>(false, idsAllowed);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE', 'ADMINISTRADOR_DE_ACCESO_DOMINIO')")
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return new ItemsAllowed<>();
	}

}
