package net.pladema.staff.controller;

import net.pladema.staff.controller.constraints.BackofficeHealthcareProfessionalEntityValidator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.repository.HealthcareProfessionalRepository;
import net.pladema.staff.repository.entity.HealthcareProfessional;

@RestController
@RequestMapping("backoffice/healthcareprofessionals")
public class BackofficeHealthcareProfessionalController
		extends AbstractBackofficeController<HealthcareProfessional, Integer> {

	public BackofficeHealthcareProfessionalController(HealthcareProfessionalRepository repository,
													  BackofficeHealthcareProfessionalEntityValidator healthcareProfessionalEntityValidator) {
		super(repository, healthcareProfessionalEntityValidator);
	}

}
