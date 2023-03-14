package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.controller.dto.PacServerTypeDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/pacservertypes")
public class BackofficePacServerTypeController extends AbstractBackofficeController<PacServerTypeDto, Short> {

    public BackofficePacServerTypeController(BackofficePacServerTypeStore store) {
		super(store);
	}

}
