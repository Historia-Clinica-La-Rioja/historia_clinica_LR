package net.pladema.user.controller;

import lombok.extern.slf4j.Slf4j;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.user.application.resetUserTwoFactorAuthentication.ResetUserTwoFactorAuthentication;
import net.pladema.user.controller.dto.BackofficeUserDto;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/users")
@Slf4j
public class BackofficeUserController extends AbstractBackofficeController<BackofficeUserDto, Integer> {

	private final ResetUserTwoFactorAuthentication resetUserTwoFactorAuthentication;

	public BackofficeUserController(
			BackofficeUsersStore backofficeUsersStore,
			BackofficeAuthoritiesValidator authoritiesValidator,
			ResetUserTwoFactorAuthentication resetUserTwoFactorAuthentication
	) {
		super(
				backofficeUsersStore,
				new BackofficeUserValidator(authoritiesValidator)
		);
		this.resetUserTwoFactorAuthentication = resetUserTwoFactorAuthentication;
	}

	@PutMapping("/{userId}/reset-2fa")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public @ResponseBody
 	Boolean resetTwoFactorAuthentication(@PathVariable("userId") Integer userId) {
		log.debug("input parameter -> userId {}", userId);
		this.resetUserTwoFactorAuthentication.run(userId);
		return true;
	}
}
