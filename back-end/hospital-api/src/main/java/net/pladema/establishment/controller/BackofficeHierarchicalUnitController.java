package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeHierarchicalUnitValidator;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/hierarchicalunits")
public class BackofficeHierarchicalUnitController extends AbstractBackofficeController<HierarchicalUnit, Integer> {

	public BackofficeHierarchicalUnitController(BackofficeHierarchicalUnitStore store,
												BackofficeHierarchicalUnitValidator validator) {
		super(store, validator);
	}

}