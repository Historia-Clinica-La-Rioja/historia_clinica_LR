package net.pladema.staff.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficePermissionValidatorAdapter;
import net.pladema.staff.controller.constraints.HealthcareProfessionalSpecialtyEntityValidator;
import net.pladema.staff.controller.mapper.BackofficeHealthcareProfessionalSpecialtyStore;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/healthcareprofessionalspecialties")
public class BackofficeHealthcareProfessionalSpecialtyController 
extends AbstractBackofficeController<HealthcareProfessionalSpecialty, Integer> {

	public BackofficeHealthcareProfessionalSpecialtyController(
			BackofficeHealthcareProfessionalSpecialtyStore backofficeHealthcareProfessionalSpecialtyStore,
			HealthcareProfessionalSpecialtyEntityValidator entityValidator)
	{
		super(backofficeHealthcareProfessionalSpecialtyStore,
				new BackofficePermissionValidatorAdapter<>(),
				entityValidator);
	}

}
