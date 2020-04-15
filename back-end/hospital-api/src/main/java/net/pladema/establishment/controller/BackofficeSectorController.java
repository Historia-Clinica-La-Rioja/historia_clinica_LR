package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/sectors")
public class BackofficeSectorController extends AbstractBackofficeController<Sector, Integer> {

	public BackofficeSectorController(SectorRepository repository) {
		super(repository);
	}

}
