package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeClinicalSpecialtySectorValidator;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.staff.controller.mapper.BackofficeClinicalSpecialtySectorStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/clinicalspecialtysectors")
public class BackofficeClinicalSpecialtySectorController extends AbstractBackofficeController<ClinicalSpecialtySector, Integer>{

	public BackofficeClinicalSpecialtySectorController(BackofficeClinicalSpecialtySectorStore repository,
													   BackofficeClinicalSpecialtySectorValidator  backofficeClinicalSpecialtySectorValidator) {
		super(repository, backofficeClinicalSpecialtySectorValidator);
	}


}
