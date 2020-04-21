package net.pladema.person.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/people")
public class BackofficePersonController extends AbstractBackofficeController<Person, Integer> {

	public BackofficePersonController(PersonRepository repository) {
		super(repository);
	}

}
