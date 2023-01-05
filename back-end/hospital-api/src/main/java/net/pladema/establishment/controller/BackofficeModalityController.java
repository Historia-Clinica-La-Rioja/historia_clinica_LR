package net.pladema.establishment.controller;

import net.pladema.establishment.repository.ModalityRepository;
import net.pladema.establishment.repository.entity.Modality;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("backoffice/modality")
public class BackofficeModalityController extends AbstractBackofficeController<Modality, Integer> {

	private final ModalityRepository repository;

	public BackofficeModalityController(ModalityRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public Modality create(@Valid @RequestBody Modality entity) {
		return super.create(entity);
	}
}

