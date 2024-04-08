package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.SnomedProcedureDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/snomedpractices")
public class BackofficeSnomedPracticesController extends AbstractBackofficeController<SnomedProcedureDto, Long> {

	public BackofficeSnomedPracticesController(BackofficeSnomedPracticesStore store) {
		super(store);
	}

}
