package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.ClinicalSpecialtySectorRepository;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

@RestController
@RequestMapping("backoffice/clinicalspecialtysectors")
public class BackofficeClinicalSpecialtySectorController extends AbstractBackofficeController<ClinicalSpecialtySector, Integer>{

	public BackofficeClinicalSpecialtySectorController(
			ClinicalSpecialtySectorRepository repository) {
		super(
				new BackofficeRepository<ClinicalSpecialtySector, Integer>(
						repository,
						new SingleAttributeBackofficeQueryAdapter<ClinicalSpecialtySector>("description")));
	}

}
