package net.pladema.person.controller;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.person.repository.PersonRepository;
import net.pladema.person.repository.entity.Person;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

@RestController
@RequestMapping("backoffice/person")
public class BackofficePersonController extends AbstractBackofficeController<Person, Integer> {

	public BackofficePersonController(PersonRepository repository) {
		super(new BackofficeRepository<Person, Integer>(
				repository,
				new BackofficeQueryAdapter<Person>() {
					@Override
					public Example<Person> buildExample(Person entity) {
						ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withMatcher("identificationNumber", GenericPropertyMatcher::startsWith)
								.withMatcher("firstName", x -> x.ignoreCase().contains())
								.withMatcher("lastName", x -> x.ignoreCase().contains());
						return Example.of(entity, matcher);
					}
				}
			)
		);
	}

}