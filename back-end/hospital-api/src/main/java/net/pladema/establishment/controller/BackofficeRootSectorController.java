package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeRootSectorValidator;
import net.pladema.establishment.repository.RootSectorRepository;
import net.pladema.establishment.repository.entity.RootSector;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/rootsectors")
public class BackofficeRootSectorController extends AbstractBackofficeController<RootSector, Integer> {

	public BackofficeRootSectorController(RootSectorRepository repository, BackofficeRootSectorValidator sectorValidator) {
		super(new BackofficeRepository<>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<>("description")
		), sectorValidator);
	}

}
