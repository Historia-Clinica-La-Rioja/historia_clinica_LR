package net.pladema.establishment.controller;

import net.pladema.person.repository.entity.Person;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

import net.pladema.snowstorm.repository.VSnomedGroupConceptRepository;
import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/snomedgroupconcepts")
public class BackofficeSnomedGroupConceptsController extends AbstractBackofficeController<VSnomedGroupConcept, Integer>{

    public BackofficeSnomedGroupConceptsController(VSnomedGroupConceptRepository repository) {
        super(new BackofficeRepository<>(
						repository,
						new BackofficeQueryAdapter<VSnomedGroupConcept>() {
							@Override
							public Example<VSnomedGroupConcept> buildExample(VSnomedGroupConcept entity) {
								ExampleMatcher matcher = ExampleMatcher
										.matching()
										.withMatcher("conceptPt", x -> x.ignoreCase().contains())
										;
								return Example.of(entity, matcher);
							}
						}
				)
		);
    }

}
