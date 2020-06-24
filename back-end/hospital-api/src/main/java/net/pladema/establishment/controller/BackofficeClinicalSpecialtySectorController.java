package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeClinicalSpecialtySectorValidator;
import net.pladema.establishment.repository.ClinicalSpecialtySectorRepository;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/clinicalspecialtysectors")
public class BackofficeClinicalSpecialtySectorController extends AbstractBackofficeController<ClinicalSpecialtySector, Integer>{

	public BackofficeClinicalSpecialtySectorController(
			ClinicalSpecialtySectorRepository repository, BackofficeClinicalSpecialtySectorValidator  backofficeClinicalSpecialtySectorValidator) {
		super(
				new BackofficeRepository<>(
						repository,
						new SingleAttributeBackofficeQueryAdapter<>("description")), backofficeClinicalSpecialtySectorValidator);
	}


}
