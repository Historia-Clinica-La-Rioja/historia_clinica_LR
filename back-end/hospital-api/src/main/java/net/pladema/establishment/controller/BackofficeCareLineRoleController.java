package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeCareLineRoleValidator;
import net.pladema.establishment.repository.entity.CareLineRole;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/carelinerole")
public class BackofficeCareLineRoleController extends AbstractBackofficeController<CareLineRole, Integer> {

	public BackofficeCareLineRoleController(BackofficeCareLineRoleStore store, BackofficeCareLineRoleValidator backofficeCareLineRoleValidator){
		super(store, backofficeCareLineRoleValidator);
	}

}
