package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeParameterValidator;
import net.pladema.establishment.controller.dto.ParameterDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/parameters")
public class BackofficeParameterController extends AbstractBackofficeController<ParameterDto, Integer> {
	public BackofficeParameterController(BackofficeParameterStore store,
										 BackofficeParameterValidator validator) {
		super(store, validator);
	}
}