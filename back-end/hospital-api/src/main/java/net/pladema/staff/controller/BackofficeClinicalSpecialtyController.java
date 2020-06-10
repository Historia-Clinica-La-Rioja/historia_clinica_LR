package net.pladema.staff.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@RestController
@RequestMapping("backoffice/clinicalspecialties")
public class BackofficeClinicalSpecialtyController extends AbstractBackofficeController<ClinicalSpecialty, Integer> {

	public BackofficeClinicalSpecialtyController(ClinicalSpecialtyRepository repository) {
		super(new BackofficeRepository<ClinicalSpecialty, Integer>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<ClinicalSpecialty>("name")
				));
	}

}
