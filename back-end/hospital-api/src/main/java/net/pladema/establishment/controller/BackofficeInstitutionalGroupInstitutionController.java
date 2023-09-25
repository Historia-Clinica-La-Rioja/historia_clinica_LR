package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.InstitutionalGroupInstitutionDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/institutionalgroupinstitutions")
public class BackofficeInstitutionalGroupInstitutionController extends AbstractBackofficeController<InstitutionalGroupInstitutionDto, Integer> {

	public BackofficeInstitutionalGroupInstitutionController(BackofficeInstitutionalGroupInstitutionStore store) {
		super(store);
	}

}
