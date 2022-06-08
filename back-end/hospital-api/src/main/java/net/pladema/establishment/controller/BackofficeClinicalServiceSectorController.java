package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeClinicalSpecialtySectorValidator;
import net.pladema.establishment.repository.ClinicalServiceSectorRepository;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/clinicalservicesectors")
public class BackofficeClinicalServiceSectorController extends AbstractBackofficeController<ClinicalSpecialtySector, Integer> {

	public BackofficeClinicalServiceSectorController(ClinicalServiceSectorRepository repository,
													 BackofficeClinicalSpecialtySectorValidator backofficeClinicalSpecialtySectorValidator) {
		super(new BackofficeRepository<>(repository, new SingleAttributeBackofficeQueryAdapter<>("description")), backofficeClinicalSpecialtySectorValidator);
	}

}
