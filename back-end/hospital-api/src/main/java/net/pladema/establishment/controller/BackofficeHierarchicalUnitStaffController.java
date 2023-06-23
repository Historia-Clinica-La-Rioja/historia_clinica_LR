package net.pladema.establishment.controller;

import net.pladema.establishment.repository.entity.HierarchicalUnitStaff;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/hierarchicalunitstaff")
public class BackofficeHierarchicalUnitStaffController extends AbstractBackofficeController<HierarchicalUnitStaff, Integer> {

	public BackofficeHierarchicalUnitStaffController(BackofficeHierarchicalUnitStaffStore store) {
		super(store);
	}

}
