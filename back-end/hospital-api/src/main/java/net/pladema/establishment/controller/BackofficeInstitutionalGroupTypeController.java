package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.InstitutionalGroupTypeDto;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/institutionalgrouptypes")
public class BackofficeInstitutionalGroupTypeController extends AbstractBackofficeController<InstitutionalGroupTypeDto, Short> {

	public BackofficeInstitutionalGroupTypeController(BackofficeInstitutionalGroupTypeStore store) {
		super(store);
	}

}
