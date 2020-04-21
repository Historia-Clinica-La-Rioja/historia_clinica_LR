package net.pladema.staff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.repository.EducationTypeRepository;
import net.pladema.staff.repository.entity.EducationType;

@RestController
@RequestMapping("backoffice/educationtypes")
public class BackofficeEducationTypeController extends AbstractBackofficeController<EducationType, Short> {

	public BackofficeEducationTypeController(EducationTypeRepository repository) {
		super(repository);
	}

}
