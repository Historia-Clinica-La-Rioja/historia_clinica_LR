package net.pladema.procedure.infrastructure.input.rest.validator.permission;

import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.procedure.infrastructure.output.repository.ProcedureTemplateRepository;
import net.pladema.procedure.infrastructure.output.repository.entity.ProcedureTemplate;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class BackofficeProcedureTemplatePermissionValidator implements BackofficePermissionValidator<ProcedureTemplate, Integer> {

	private final BackofficeAuthoritiesValidator authoritiesValidator;

	private ProcedureTemplateRepository repository;

	@Override
	public void assertGetList(ProcedureTemplate entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return ids;
		return List.of();
	}

	@Override
	public void assertGetOne(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public void assertCreate(ProcedureTemplate entity) {
		assertIdIsNull(entity);
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public void assertUpdate(Integer id, ProcedureTemplate entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public void assertDelete(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;

	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList(ProcedureTemplate entity) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();
		return new ItemsAllowed<>(false, Collections.emptyList());
	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList() {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return new ItemsAllowed<>();
		return new ItemsAllowed<>(false, Collections.emptyList());
	}

	private void assertIdIsNull(ProcedureTemplate procedureTemplate) {
		if (procedureTemplate.getId() != null) {
			throw new BackofficeValidationException("El campo id debe ser vacío");
		}
	}

}
