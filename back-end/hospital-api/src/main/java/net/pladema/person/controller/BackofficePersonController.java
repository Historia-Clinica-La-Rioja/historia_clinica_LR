package net.pladema.person.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.person.repository.BackofficePersonDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/person")
public class BackofficePersonController extends AbstractBackofficeController<BackofficePersonDto, Integer> {

	public BackofficePersonController(BackofficePersonStore store) {
		super(store);
	}



}