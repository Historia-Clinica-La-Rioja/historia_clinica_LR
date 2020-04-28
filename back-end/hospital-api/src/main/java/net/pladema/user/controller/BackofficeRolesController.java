package net.pladema.user.controller;

import net.pladema.permissions.repository.entity.Role;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/roles")
public class BackofficeRolesController extends AbstractBackofficeController<Role, Short> {
	public BackofficeRolesController(
			BackofficeRolesStore store
	) {
		super(
				store,
				new BackofficePermissionValidatorAdapter<>(HttpMethod.GET)
		);
	}
}
