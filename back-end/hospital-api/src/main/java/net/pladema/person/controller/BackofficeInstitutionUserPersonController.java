package net.pladema.person.controller;

import lombok.extern.slf4j.Slf4j;

import net.pladema.person.controller.dto.InstitutionUserPersonDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("backoffice/institutionuserpersons")
@RestController
public class BackofficeInstitutionUserPersonController extends AbstractBackofficeController<InstitutionUserPersonDto, Integer> {

	public BackofficeInstitutionUserPersonController(BackofficeInstitutionUserPersonStore store){
		super(store);
	}

}
