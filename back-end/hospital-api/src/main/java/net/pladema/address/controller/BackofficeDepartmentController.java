package net.pladema.address.controller;

import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.address.repository.DepartmentRepository;
import net.pladema.address.repository.entity.Department;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/departments")
public class BackofficeDepartmentController extends AbstractBackofficeController<Department, Short> {

	public BackofficeDepartmentController(DepartmentRepository repository) {
		super(new BackofficeRepository<Department, Short>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<Department>("description")
		));
	}


}
