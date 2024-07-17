package net.pladema.parameterizedform.infrastructure.input.rest.constraints.validator;

import lombok.AllArgsConstructor;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class BackofficeParameterizedFormValidator implements BackofficePermissionValidator<ParameterizedForm, Integer> {

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetList(ParameterizedForm entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return ids;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertGetOne(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertCreate(ParameterizedForm entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertUpdate(Integer id, ParameterizedForm entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public void assertDelete(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed<Integer> itemsAllowedToList(ParameterizedForm entity) {
		return new ItemsAllowed<>();
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return new ItemsAllowed<>();
	}
}
