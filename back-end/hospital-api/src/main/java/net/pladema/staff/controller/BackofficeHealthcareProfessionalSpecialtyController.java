package net.pladema.staff.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.controller.constraints.HealthcareProfessionalSpecialtyEntityValidator;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/healthcareprofessionalspecialties")
public class BackofficeHealthcareProfessionalSpecialtyController 
extends AbstractBackofficeController<HealthcareProfessionalSpecialty, Integer> {

	public BackofficeHealthcareProfessionalSpecialtyController(
			HealthcareProfessionalSpecialtyRepository repository,
			HealthcareProfessionalSpecialtyEntityValidator entityValidator) {
		super(repository, entityValidator);
	}

}
