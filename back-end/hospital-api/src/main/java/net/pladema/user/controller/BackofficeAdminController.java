package net.pladema.user.controller;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.user.controller.dto.AdminUserDto;

@RestController
@RequestMapping("backoffice/admin")
public class BackofficeAdminController extends AbstractBackofficeController<AdminUserDto, Integer> {
	public BackofficeAdminController(
			BackofficeAdminStore backofficeAdminStore
	) {
		super(
				backofficeAdminStore,
				new BackofficePermissionValidatorAdapter<>(HttpMethod.GET)
		);
	}
}
