package net.pladema.staff.controller;

import net.pladema.staff.controller.mapper.BackofficeClinicalSpecialtyStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@RestController
@RequestMapping("backoffice/clinicalspecialties")
public class BackofficeClinicalSpecialtyController extends AbstractBackofficeController<ClinicalSpecialty, Integer> {

	public BackofficeClinicalSpecialtyController(BackofficeClinicalSpecialtyStore backofficeClinicalSpecialtyStore) {
		super(backofficeClinicalSpecialtyStore);
	}

}

