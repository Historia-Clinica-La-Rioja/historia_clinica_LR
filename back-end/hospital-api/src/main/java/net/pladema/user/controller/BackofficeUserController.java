package net.pladema.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.user.application.resetUserTwoFactorAuthentication.ResetUserTwoFactorAuthentication;
import net.pladema.user.controller.dto.BackofficeUserDto;

@RestController
@RequestMapping("backoffice/users")
@Slf4j
public class BackofficeUserController extends AbstractBackofficeController<BackofficeUserDto, Integer> {

	private final ResetUserTwoFactorAuthentication resetUserTwoFactorAuthentication;

	private final BackofficeUsersStore backofficeUsersStore;

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
		this.backofficeUsersStore = backofficeUsersStore;
	}

	@PutMapping("/{userId}/reset-2fa")
	@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR', 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public @ResponseBody
 	Boolean resetTwoFactorAuthentication(@PathVariable("userId") Integer userId) {
		log.debug("input parameter -> userId {}", userId);
		this.resetUserTwoFactorAuthentication.run(userId);
		return true;
	}

	@Override
	public BackofficeUserDto create(@RequestBody BackofficeUserDto entity) {
		if(backofficeUsersStore.findByUsername(entity.getUsername()).isPresent())
			throw new BackofficeValidationException(String.format("El nombre de usuario %s no está disponible", entity.getUsername()));
		return super.create(entity);
	}
}
