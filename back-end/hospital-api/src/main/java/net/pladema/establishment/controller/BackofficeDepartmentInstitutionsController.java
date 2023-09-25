package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.DepartmentInstitutionDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/departmentinstitutions")
public class BackofficeDepartmentInstitutionsController extends AbstractBackofficeController<DepartmentInstitutionDto, Integer> {

	public BackofficeDepartmentInstitutionsController(BackofficeDepartmentInstitutionsStore store){
		super(store);
	}

}
