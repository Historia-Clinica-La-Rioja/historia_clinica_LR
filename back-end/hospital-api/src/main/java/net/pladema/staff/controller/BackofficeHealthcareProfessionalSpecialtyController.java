package net.pladema.staff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;

@RestController
@RequestMapping("backoffice/healthcareprofessionalspecialties")
public class BackofficeHealthcareProfessionalSpecialtyController 
extends AbstractBackofficeController<HealthcareProfessionalSpecialty, Integer> {

	public BackofficeHealthcareProfessionalSpecialtyController(
			HealthcareProfessionalSpecialtyRepository repository) {
		super(repository);
	}

}
