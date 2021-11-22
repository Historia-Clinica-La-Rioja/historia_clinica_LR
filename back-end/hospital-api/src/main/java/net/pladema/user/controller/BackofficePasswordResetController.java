package net.pladema.user.controller;

import ar.lamansys.sgx.auth.user.infrastructure.output.userpassword.PasswordResetToken;
import ar.lamansys.sgx.auth.user.infrastructure.output.userpassword.PasswordResetTokenRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("backoffice/password-reset")
public class BackofficePasswordResetController extends AbstractBackofficeController<PasswordResetToken, Long> {

	public BackofficePasswordResetController(
			PasswordResetTokenRepository repository,
			BackofficeAuthoritiesValidator authoritiesValidator
	) {
		super(
				repository,
				new BackofficePasswordResetValidator(authoritiesValidator)
		);
	}

	@Override
	public PasswordResetToken create(@Valid @RequestBody PasswordResetToken entity) {
		entity.setExpiryDate(LocalDateTime.now().plusDays(2));
		entity.setToken(UUID.randomUUID().toString());
		entity.setEnable(true);
		return super.create(entity);
	}
}
