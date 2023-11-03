package net.pladema.establishment.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/clinicalspecialtyrules")
public class BackofficeClinicalSpecialtyRuleController extends AbstractBackofficeController <ClinicalSpecialty, Integer> {

	public BackofficeClinicalSpecialtyRuleController(BackofficeClinicalSpecialtyRuleStore store){
		super(store);
	}

}
