package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.establishment.repository.entity.Institution;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/establishment/institutions")
public class BackofficeInstitutionController extends AbstractBackofficeController<Institution, Integer> {

	public BackofficeInstitutionController(InstitutionRepository repository) {
		super(repository);
	}

}
