package net.pladema.address.controller;

import net.pladema.address.repository.CityRepository;
import net.pladema.address.repository.entity.City;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.AclCrudPermissionValidatorAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/cities")
public class BackofficeCityController extends AbstractBackofficeController<City, Integer> {

	public BackofficeCityController(
			CityRepository repository
	) {
		super(repository);
	}


}
