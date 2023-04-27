package net.pladema.establishment.controller;

import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.auth.twoWayEncryption.infrastructure.output.TwoWayEncryptionService;
import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserEnumException;
import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserException;
import net.pladema.establishment.repository.PacServerRepository;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@RestController
@RequestMapping("backoffice/pacservers")
@PreAuthorize("hasAnyAuthority('ROOT')")
public class BackofficePacServerController extends AbstractBackofficeController<PacServer, Integer> {

	private final int SERVIDOR_CENTRAL = 1;

	private final int CENTRO_DE_DIAGNOSTICO = 2;
	private final Pattern usernamePattern;

	private final TwoWayEncryptionService twoWayEncryptionService;

	public BackofficePacServerController(PacServerRepository repository, @Value("${authentication.username.pattern:.+}") String pattern, TwoWayEncryptionService twoWayEncryptionService) {
		super(repository);
		this.usernamePattern = Pattern.compile(pattern);
		this.twoWayEncryptionService = twoWayEncryptionService;
	}

	@Override
	public PacServer getOne(@PathVariable("id") Integer id) {
		var entity = super.getOne(id);
		try {
			entity.setPassword(twoWayEncryptionService.decrypt(entity.getPassword()));
		} catch (Exception e) {
			entity.setPassword(null);
		}
		return entity;
	}

	@Override
	public PacServer update(@PathVariable("id") Integer id, @RequestBody PacServer entity) {
		validations(entity);
		if (entity.getPacServerType() == SERVIDOR_CENTRAL){
			entity = new PacServer(entity.getId(),
						entity.getName(),
						entity.getAetitle(),
						entity.getDomain(),
						entity.getPacServerType(),
						entity.getPacServerProtocol(),
						entity.getUsername(),
						entity.getPassword(),
						entity.getUrlStow(),
						entity.getUrlAuth(),
						null);
		}
		try {
			entity.setPassword(twoWayEncryptionService.encrypt(entity.getPassword()));
		} catch (Exception e) {
			throw new BackofficeValidationException("Contraseña invalida");
		}
		return super.update(id, entity);
	}

	@Override
	public PacServer create(@Valid @RequestBody PacServer entity) {
		validations(entity);
		try {
			entity.setPassword(twoWayEncryptionService.encrypt(entity.getPassword()));
		} catch (Exception e) {
			throw new BackofficeValidationException("Contraseña invalida");
		}
		return super.create(entity);
	}

	private void validations(PacServer entity) {
		if(entity.getPacServerType() == null)
			throw new BackofficeValidationException("No se definió el tipo de servidor");
		if(entity.getPacServerType() == CENTRO_DE_DIAGNOSTICO && entity.getInstitutionId() == null)
			throw new BackofficeValidationException("Un Centro de diagnóstico debe tener asociada una institución");
		if(entity.getPacServerProtocol() == null)
			throw new BackofficeValidationException("No se definió el protocolo de imagen");
		var username = entity.getUsername();
		if(username == null)
			throw new RegisterUserException(RegisterUserEnumException.NULL_USERNAME, "El nombre de usuario es obligatorio");
		if (!usernamePattern.matcher(username).matches()){
			throw new RegisterUserException(RegisterUserEnumException.INVALID_USERNAME_PATTERN,
					String.format("El username %s no cumple con el patrón %s obligatorio", username, usernamePattern.pattern()));
		}
		var password = entity.getPassword();
		if(password == null || password.isBlank())
			throw new BackofficeValidationException("Contraseña invalida");
	}
}
