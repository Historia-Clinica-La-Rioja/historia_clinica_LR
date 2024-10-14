package net.pladema.parameter.infrastructure.input.rest;

import net.pladema.parameter.infrastructure.output.BackofficeParameterStore;
import net.pladema.parameter.infrastructure.input.rest.constraints.validator.BackofficeParameterValidator;
import net.pladema.parameter.infrastructure.input.rest.dto.ParameterDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("backoffice/parameters")
@RestController
public class BackofficeParameterController extends AbstractBackofficeController<ParameterDto, Integer> {
	public BackofficeParameterController(BackofficeParameterStore store,
                                         BackofficeParameterValidator validator) {
		super(store, validator);
	}
}