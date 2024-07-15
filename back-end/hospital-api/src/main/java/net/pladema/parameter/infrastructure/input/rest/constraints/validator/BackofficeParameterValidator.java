package net.pladema.parameter.infrastructure.input.rest.constraints.validator;

import lombok.AllArgsConstructor;
import net.pladema.parameter.infrastructure.input.rest.dto.ParameterDto;
import net.pladema.parameter.infrastructure.output.repository.ParameterRepository;
import net.pladema.parameter.infrastructure.output.repository.entity.Parameter;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormParameterRepository;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class BackofficeParameterValidator implements BackofficePermissionValidator<ParameterDto, Integer> {

	private final ParameterRepository parameterRepository;
	private final ParameterizedFormParameterRepository parameterizedFormParameterRepository;

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertGetList(ParameterDto entity) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return ids;
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertGetOne(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertCreate(ParameterDto entity) {
		assertIdIsNull(entity);
		assertLoincId(entity);
		assertDescription(entity);
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertUpdate(Integer id, ParameterDto entity) {
		Optional<Parameter> oldParameterOp = parameterRepository.findById(id);
		if (oldParameterOp.isPresent()) {
			Parameter oldParameter = oldParameterOp.get();

			if (parameterizedFormParameterRepository.isAssociatedAnActiveParameterizedForm(oldParameter.getId())) {
				throw new BackofficeValidationException("No puede editarse el parametro porque esta asociado a un formulario");
			}

			if (entity.getLoincId() != null && !entity.getLoincId().equals(oldParameter.getLoincId())) {
				assertLoincId(entity);
			}

			if (entity.getDescription() != null && !entity.getDescription().equals(oldParameter.getDescription())) {
				assertDescription(entity);
			}
		}
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public void assertDelete(Integer id) {

	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public ItemsAllowed<Integer> itemsAllowedToList(ParameterDto entity) {
		return new ItemsAllowed<>();
	}

	@Override
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
	public ItemsAllowed<Integer> itemsAllowedToList() {
		return new ItemsAllowed<>();
	}

	private void assertIdIsNull(ParameterDto parameterDto) {
		if (parameterDto.getId() != null) {
			throw new BackofficeValidationException("El campo id debe ser vacío.");
		}
	}

	private void assertLoincId(ParameterDto parameterDto) {
		if (parameterDto.getLoincId() != null)
			if (parameterRepository.existsParameterByLoincId(parameterDto.getLoincId()))
				throw new BackofficeValidationException("Ya existe un parametro con ese codigo LOINC.");
	}

	private void assertDescription(ParameterDto parameterDto) {
		String description = parameterDto.getDescription();
		if (description != null)
			if (parameterRepository.existsParameterByDescription(description))
				throw new BackofficeValidationException("Ya existe un parametro con esa descripcion.");
	}
}
