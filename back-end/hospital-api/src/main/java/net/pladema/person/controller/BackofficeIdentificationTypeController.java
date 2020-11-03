package net.pladema.person.controller;

import net.pladema.person.repository.IdentificationTypeRepository;
import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.IdentificationType;
import net.pladema.person.repository.entity.Person;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/identificationTypes")
public class BackofficeIdentificationTypeController extends AbstractBackofficeController<IdentificationType, Short> {

	public BackofficeIdentificationTypeController(IdentificationTypeRepository repository) {
		super(repository);
	}

}