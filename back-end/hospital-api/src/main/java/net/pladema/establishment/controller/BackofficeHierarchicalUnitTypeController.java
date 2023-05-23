package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeHierarchicalUnitTypeValidator;
import net.pladema.establishment.repository.entity.HierarchicalUnitType;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/hierarchicalunittypes")
public class BackofficeHierarchicalUnitTypeController  extends AbstractBackofficeController<HierarchicalUnitType, Integer> {

	public BackofficeHierarchicalUnitTypeController(BackofficeHierarchicalUnitTypeStore store,
													BackofficeHierarchicalUnitTypeValidator validator) {
		super(store, validator);
	}

}
