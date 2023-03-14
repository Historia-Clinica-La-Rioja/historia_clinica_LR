package net.pladema.establishment.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.PacServerImageLvlRepository;
import net.pladema.establishment.repository.entity.PacServerImageLvl;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@RestController
@RequestMapping("backoffice/pacserversimagelvl")
public class BackofficePacServerImageLvlController extends AbstractBackofficeController<PacServerImageLvl, Integer> {

	private final PacServerImageLvlRepository repository;

	public BackofficePacServerImageLvlController(PacServerImageLvlRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public PacServerImageLvl create(@Valid @RequestBody PacServerImageLvl entity) {
		if (repository.countElementsBySector(entity.getSectorId()) < 1)
			return super.create(entity);
		throw new BackofficeValidationException(String.format("El sector #%s ya posee un Servidor PAC", entity.getSectorId()));
	}
}
