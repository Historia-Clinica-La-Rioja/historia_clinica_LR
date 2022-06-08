package net.pladema.staff.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.controller.mapper.BackofficeClinicalServiceStore;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/clinicalservices")
public class BackofficeClinicalServiceController extends AbstractBackofficeController<ClinicalSpecialty, Integer> {

	public BackofficeClinicalServiceController(BackofficeClinicalServiceStore backofficeClinicalServiceStore) {
		super(backofficeClinicalServiceStore);
	}

}
