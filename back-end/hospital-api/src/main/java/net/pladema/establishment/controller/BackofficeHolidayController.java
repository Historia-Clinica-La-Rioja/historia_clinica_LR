package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.entities.BackofficeHolidayEntityValidator;
import net.pladema.establishment.repository.HolidayRepository;
import net.pladema.establishment.repository.entity.Holiday;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/holidays")
public class BackofficeHolidayController extends AbstractBackofficeController<Holiday, Integer> {

	public BackofficeHolidayController(HolidayRepository repository, BackofficeHolidayEntityValidator backofficeHolidayEntityValidator) {
		super(repository, backofficeHolidayEntityValidator);
	}
	
}
