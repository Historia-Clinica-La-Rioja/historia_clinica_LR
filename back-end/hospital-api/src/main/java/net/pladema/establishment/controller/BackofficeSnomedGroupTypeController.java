package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.snowstorm.repository.SnomedGroupTypeRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroupType;

@RestController
@RequestMapping("backoffice/snomedgrouptypes")
public class BackofficeSnomedGroupTypeController extends AbstractBackofficeController<SnomedGroupType, Short> {

	public BackofficeSnomedGroupTypeController(SnomedGroupTypeRepository repository) {
		super(repository);
	}
}
