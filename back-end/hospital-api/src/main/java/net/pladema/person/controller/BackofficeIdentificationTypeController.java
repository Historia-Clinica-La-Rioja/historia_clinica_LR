package net.pladema.person.controller;

import net.pladema.person.repository.IdentificationTypeRepository;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/identificationTypes")
public class BackofficeIdentificationTypeController extends AbstractBackofficeController<IdentificationType, Short> {

	public BackofficeIdentificationTypeController(IdentificationTypeRepository repository) {
		super(repository);
	}

}