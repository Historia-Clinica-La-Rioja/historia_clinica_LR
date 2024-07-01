package net.pladema.parameterizedform.infrastructure.input.rest;

import net.pladema.parameterizedform.infrastructure.input.rest.constraints.validator.BackofficeParameterizedFormParameterValidator;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormParameterDto;
import net.pladema.parameterizedform.infrastructure.output.BackofficeParameterizedFormParameterStore;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("backoffice/parameterizedformparameter")
@RestController
public class BackofficeParameterizedFormParameterController extends AbstractBackofficeController<ParameterizedFormParameterDto, Integer> {

	public BackofficeParameterizedFormParameterController(BackofficeParameterizedFormParameterStore store,
														  BackofficeParameterizedFormParameterValidator validator) {
		super(store, validator);
	}
}
