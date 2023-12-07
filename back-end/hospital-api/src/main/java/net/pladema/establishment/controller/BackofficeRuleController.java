package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeRuleValidator;
import net.pladema.establishment.controller.dto.RuleDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/rules")
public class BackofficeRuleController extends AbstractBackofficeController<RuleDto, Integer> {

	public BackofficeRuleController(BackofficeRuleStore store, BackofficeRuleValidator validator){
		super(store, new BackofficePermissionValidatorAdapter<>(), validator);
	}
}
