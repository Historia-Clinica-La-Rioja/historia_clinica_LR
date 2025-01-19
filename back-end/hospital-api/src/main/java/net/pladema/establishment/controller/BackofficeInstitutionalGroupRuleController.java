package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeInstitutionalGroupRuleValidator;
import net.pladema.establishment.controller.dto.InstitutionalGroupRuleDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/institutionalgrouprules")
public class BackofficeInstitutionalGroupRuleController extends AbstractBackofficeController<InstitutionalGroupRuleDto, Integer> {

	public BackofficeInstitutionalGroupRuleController(BackofficeInstitutionalGroupRuleStore store, BackofficeInstitutionalGroupRuleValidator validator){
		super(store, new BackofficePermissionValidatorAdapter<>(), validator);
	}
	
}
