package net.pladema.person.controller;

import net.pladema.person.controller.dto.ManagerUserPersonDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/manageruserpersons")
public class BackofficeManagerUserPersonController extends AbstractBackofficeController<ManagerUserPersonDto, Integer> {

	public BackofficeManagerUserPersonController(BackofficeManagerUserPersonStore store){
		super(store);
	}
}
