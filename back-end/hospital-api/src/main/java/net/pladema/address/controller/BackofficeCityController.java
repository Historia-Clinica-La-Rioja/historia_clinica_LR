package net.pladema.address.controller;

import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.address.repository.CityRepository;
import net.pladema.address.repository.entity.City;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/cities")
public class BackofficeCityController extends AbstractBackofficeController<City, Integer> {

	public BackofficeCityController(CityRepository repository) {
		super(new BackofficeRepository<City, Integer>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<City>("description")
		));
	}


}
