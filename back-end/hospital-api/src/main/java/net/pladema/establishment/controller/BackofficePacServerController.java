package net.pladema.establishment.controller;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserEnumException;
import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserException;
import ar.lamansys.sgx.auth.user.domain.userpassword.PasswordEncryptor;
import net.pladema.establishment.repository.PacServerRepository;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@RestController
@RequestMapping("backoffice/pacservers")
public class BackofficePacServerController extends AbstractBackofficeController<PacServer, Integer> {

	private final PasswordEncryptor passwordEncryptor;

	private final Pattern usernamePattern;

	public BackofficePacServerController(PacServerRepository repository, PasswordEncryptor passwordEncryptor,
										 @Value("${authentication.username.pattern:.+}") String pattern) {
		super(repository);
		this.passwordEncryptor = passwordEncryptor;
		this.usernamePattern = Pattern.compile(pattern);
	}

	@Override
	public PacServer create(@Valid @RequestBody PacServer entity) {
		validations(entity);
		var salt = "salt";
		var hashAlgorithm = "hashAlgorithm";
		var encodedPassword = passwordEncryptor.encode(entity.getPassword(), salt, hashAlgorithm);
		entity.setPassword(encodedPassword);
		return super.create(entity);
	}

	private void validations(PacServer entity) {
		var username = entity.getUsername();
		Objects.requireNonNull(username, () -> {
			throw new RegisterUserException(RegisterUserEnumException.NULL_USERNAME, "El username es obligatorio");
		});
		if (!usernamePattern.matcher(username).matches()){
			throw new RegisterUserException(RegisterUserEnumException.INVALID_USERNAME_PATTERN,
					String.format("El username %s no cumple con el patrón %s obligatorio", username, usernamePattern.pattern()));
		}
		if(entity.getPacServerType() == null)
			throw new BackofficeValidationException("No se definió el tipo de servidor");
		if(entity.getPacServerProtocol() == null)
			throw new BackofficeValidationException("No se definió el protocolo de imagen");
	}
}
