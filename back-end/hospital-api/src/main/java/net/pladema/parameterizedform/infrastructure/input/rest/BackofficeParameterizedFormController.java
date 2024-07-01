package net.pladema.parameterizedform.infrastructure.input.rest;

import net.pladema.parameterizedform.infrastructure.input.rest.constraints.validator.BackofficeParameterizedFormValidator;
import net.pladema.parameterizedform.infrastructure.output.BackofficeParameterizedFormStore;
import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("backoffice/parameterizedform")
@RestController
public class BackofficeParameterizedFormController extends AbstractBackofficeController<ParameterizedForm, Integer> {
	public BackofficeParameterizedFormController(BackofficeParameterizedFormStore store,
												 BackofficeParameterizedFormValidator validator) {
		super(store, validator);
	}
}
