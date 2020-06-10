package net.pladema.staff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import net.pladema.staff.repository.ProfessionalSpecialtyRepository;
import net.pladema.staff.repository.entity.ProfessionalSpecialty;

@RestController
@RequestMapping("backoffice/professionalspecialties")
public class BackofficeProfessionalSpecialtyController extends AbstractBackofficeController<ProfessionalSpecialty, Integer> {

	public BackofficeProfessionalSpecialtyController(ProfessionalSpecialtyRepository repository) {
		super(new BackofficeRepository<ProfessionalSpecialty, Integer>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<ProfessionalSpecialty>("description")
				));
	}

}
