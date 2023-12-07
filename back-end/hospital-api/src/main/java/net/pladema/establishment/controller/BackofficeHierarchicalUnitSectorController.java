package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeHierarchicalUnitSectorValidator;
import net.pladema.establishment.repository.HierarchicalUnitSectorRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnitSector;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/hierarchicalunitsectors")
public class BackofficeHierarchicalUnitSectorController extends AbstractBackofficeController<HierarchicalUnitSector, Integer> {

	public BackofficeHierarchicalUnitSectorController(HierarchicalUnitSectorRepository repository, BackofficeHierarchicalUnitSectorValidator backofficeHierarchicalUnitSectorValidator)
	{
		super(new BackofficeRepository<>(repository), backofficeHierarchicalUnitSectorValidator);
	}

}
