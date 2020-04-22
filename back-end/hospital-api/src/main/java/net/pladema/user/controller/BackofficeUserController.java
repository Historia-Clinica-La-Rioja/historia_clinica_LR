package net.pladema.user.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.user.controller.dto.BackofficeUserDto;
import net.pladema.user.controller.mapper.BackofficeUsersStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/users")
public class BackofficeUserController extends AbstractBackofficeController<BackofficeUserDto, Integer> {
	public BackofficeUserController(
			BackofficeUsersStore backofficeUsersStore,
			BackofficeAuthoritiesValidator authoritiesValidator
	) {
		super(
				backofficeUsersStore,
				new BackofficeUserValidator(authoritiesValidator)
		);
	}
}
