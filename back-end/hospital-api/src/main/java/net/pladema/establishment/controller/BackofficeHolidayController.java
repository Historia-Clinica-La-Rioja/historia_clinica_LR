package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.entities.BackofficeHolidayEntityValidator;
import net.pladema.establishment.repository.HolidayRepository;
import net.pladema.establishment.repository.entity.Holiday;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/holidays")
public class BackofficeHolidayController extends AbstractBackofficeController<Holiday, Integer> {

	public BackofficeHolidayController(HolidayRepository repository, BackofficeHolidayEntityValidator backofficeHolidayEntityValidator) {
		super(new BackofficeRepository<Holiday, Integer>(
				repository,
				new BackofficeQueryAdapter<Holiday>() {
					@Override
					public Example<Holiday> buildExample(Holiday entity) {
						ExampleMatcher matcher = ExampleMatcher
								.matching()
								.withMatcher("description", x -> x.ignoreCase().contains());
						return Example.of(entity, matcher);
					}
				}),
				new BackofficePermissionValidatorAdapter<>(),
				backofficeHolidayEntityValidator);
	}
	
}
