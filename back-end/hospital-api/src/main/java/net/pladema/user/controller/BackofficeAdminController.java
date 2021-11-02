package net.pladema.user.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.user.controller.dto.BackofficeUserDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/admin")
public class BackofficeAdminController extends AbstractBackofficeController<BackofficeUserDto, Integer> {
	public BackofficeAdminController(
			BackofficeUsersStore backofficeUsersStore,
			BackofficeAuthoritiesValidator authoritiesValidator
	) {
		super(
				backofficeUsersStore,
				new BackofficeUserValidator(authoritiesValidator)
		);
	}
}
