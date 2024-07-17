package net.pladema.parameterizedform.infrastructure.input.rest;

import net.pladema.parameterizedform.application.UpdateFormParameterOrder;
import net.pladema.parameterizedform.infrastructure.input.rest.constraints.validator.BackofficeParameterizedFormParameterValidator;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormParameterDto;
import net.pladema.parameterizedform.infrastructure.output.BackofficeParameterizedFormParameterStore;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("backoffice/parameterizedformparameter")
@RestController
public class BackofficeParameterizedFormParameterController extends AbstractBackofficeController<ParameterizedFormParameterDto, Integer> {

	private static final String UP = "up";
	private static final String DOWN = "down";
	UpdateFormParameterOrder updateFormParameterOrder;

	public BackofficeParameterizedFormParameterController(BackofficeParameterizedFormParameterStore store,
														  BackofficeParameterizedFormParameterValidator validator,
														  UpdateFormParameterOrder updateFormParameterOrder) {
		super(store, validator);
		this.updateFormParameterOrder = updateFormParameterOrder;
	}

	@PutMapping(value = "/{id}/change-order")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR','ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	@ResponseStatus(HttpStatus.OK)
	public void changeOrder(@PathVariable Integer id, @RequestParam String direction) {

		if (direction.equals(UP)) {
			updateFormParameterOrder.run(id, (x) -> (short) (x + 1));
		}

		if (direction.equals(DOWN)) {
			updateFormParameterOrder.run(id, (x) -> (short) (x - 1));
		}
	}
}
