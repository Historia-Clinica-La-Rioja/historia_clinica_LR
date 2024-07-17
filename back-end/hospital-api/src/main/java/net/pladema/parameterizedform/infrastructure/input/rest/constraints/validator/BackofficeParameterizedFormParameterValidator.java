package net.pladema.parameterizedform.infrastructure.input.rest.constraints.validator;


import lombok.AllArgsConstructor;

import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormParameterDto;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormParameterRepository;

import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class BackofficeParameterizedFormParameterValidator implements BackofficePermissionValidator<ParameterizedFormParameterDto, Integer> {

	private final ParameterizedFormParameterRepository parameterizedFormParameterRepository;

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetList(ParameterizedFormParameterDto entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return List.of();
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetOne(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertCreate(ParameterizedFormParameterDto entity) {
		Boolean parameterAlreadyAssociated = parameterizedFormParameterRepository.alreadyExistsParameterizedFormParameters(entity.getParameterizedFormId(), entity.getParameterId());
		if (parameterAlreadyAssociated)
			throw new BackofficeValidationException("El parametro ya se encuentra asociado al formulario");

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertUpdate(Integer id, ParameterizedFormParameterDto entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertDelete(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed<Integer> itemsAllowedToList(ParameterizedFormParameterDto entity) {
		return new ItemsAllowed<>();
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return new ItemsAllowed<>();
	}
}
