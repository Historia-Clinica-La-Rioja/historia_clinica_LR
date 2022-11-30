package net.pladema.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;

@RestController
@RequestMapping("backoffice/userroles")
public class BackofficeUserRolesController extends AbstractBackofficeController<UserRole, Long> {
	public BackofficeUserRolesController(
			BackofficeUserRolesStore store
	) {
		super(
				store,
				new BackofficePermissionValidatorAdapter<>()
		);
	}
}
