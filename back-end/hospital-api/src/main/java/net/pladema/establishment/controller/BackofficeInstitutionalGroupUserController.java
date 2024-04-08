package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeInstitutionalGroupUserValidator;
import net.pladema.establishment.repository.InstitutionalGroupUserRepository;
import net.pladema.establishment.repository.entity.InstitutionalGroupUser;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/institutionalgroupusers")
public class BackofficeInstitutionalGroupUserController extends AbstractBackofficeController<InstitutionalGroupUser, Integer> {

	public BackofficeInstitutionalGroupUserController(InstitutionalGroupUserRepository repository, BackofficeInstitutionalGroupUserValidator validator){
		super(new BackofficeRepository<>(repository), new BackofficePermissionValidatorAdapter<>(), validator);
	}

}
