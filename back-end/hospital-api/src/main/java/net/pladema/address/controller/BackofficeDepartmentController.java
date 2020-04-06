package net.pladema.address.controller;

import net.pladema.address.repository.DepartmentRepository;
import net.pladema.address.repository.entity.Department;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.AclCrudPermissionValidatorAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/departments")
public class BackofficeDepartmentController extends AbstractBackofficeController<Department, Short> {

	public BackofficeDepartmentController(
			DepartmentRepository repository
	) {
		super(repository);
	}


}
