package net.pladema.procedure.infrastructure.input.rest.validator.permission;

import lombok.AllArgsConstructor;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.procedure.infrastructure.input.rest.dto.ProcedureParameterDto;
import net.pladema.procedure.infrastructure.output.repository.ProcedureParameterRepository;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class BackofficeProcedureParameterValidator implements BackofficePermissionValidator<ProcedureParameterDto,Integer> {

	private final BackofficeAuthoritiesValidator authoritiesValidator;
	private final ProcedureParameterRepository procedureParameterRepository;

	@Override
	public void assertGetList(ProcedureParameterDto entity) {
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
	public void assertCreate(ProcedureParameterDto entity) {
		assertIdIsNull(entity);
		assertTemplateIdAndLoinc(entity);
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public void assertUpdate(Integer id, ProcedureParameterDto entity) {
		assertTemplateIdAndLoinc(entity);
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public void assertDelete(Integer id) {
		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return;
	}

	@Override
	public ItemsAllowed<Integer> itemsAllowedToList(ProcedureParameterDto entity) {
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

	private void assertIdIsNull(ProcedureParameterDto procedureParameterDto) {
		if (procedureParameterDto.getId() != null) {
			throw new BackofficeValidationException("El campo id debe ser vacío.");
		}
	}

	private void assertTemplateIdAndLoinc(ProcedureParameterDto procedureParameterDto) {
		if (procedureParameterDto.getLoincId() != null && procedureParameterDto.getProcedureTemplateId() != null)
			if (procedureParameterRepository.templateHasSpecificLoincId(procedureParameterDto.getProcedureTemplateId(), procedureParameterDto.getLoincId()))
				throw new BackofficeValidationException("Ya esta asociado el parametro a este template.");
	}
}
