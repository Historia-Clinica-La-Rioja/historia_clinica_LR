package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeClinicalServiceSectorValidator;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.staff.controller.mapper.BackofficeClinicalServiceSectorStore;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/clinicalservicesectors")
public class BackofficeClinicalServiceSectorController extends AbstractBackofficeController<ClinicalSpecialtySector, Integer> {

	public BackofficeClinicalServiceSectorController(BackofficeClinicalServiceSectorStore repository,
													 BackofficeClinicalServiceSectorValidator backofficeClinicalSpecialtySectorValidator) {
		super(repository, backofficeClinicalSpecialtySectorValidator);
	}

}
