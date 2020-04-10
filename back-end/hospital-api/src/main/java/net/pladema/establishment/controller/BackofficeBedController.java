package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/establishment/beds")
public class BackofficeBedController extends AbstractBackofficeController<Bed, Integer> {

	public BackofficeBedController(BedRepository repository) {
		super(repository);
	}

}
