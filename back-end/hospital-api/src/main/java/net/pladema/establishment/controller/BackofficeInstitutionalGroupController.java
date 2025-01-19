package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeInstitutionalGroupValidator;
import net.pladema.establishment.controller.dto.InstitutionalGroupDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/institutionalgroups")
public class BackofficeInstitutionalGroupController extends AbstractBackofficeController<InstitutionalGroupDto, Integer> {

	public BackofficeInstitutionalGroupController(BackofficeInstitutionalGroupStore store,
												  BackofficeInstitutionalGroupValidator backofficeInstitutionalGroupValidator) {
		super(store, new BackofficePermissionValidatorAdapter<>(), backofficeInstitutionalGroupValidator);
	}

}
