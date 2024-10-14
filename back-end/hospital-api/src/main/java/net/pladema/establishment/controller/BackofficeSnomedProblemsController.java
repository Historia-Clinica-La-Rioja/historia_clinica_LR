package net.pladema.establishment.controller;


import net.pladema.establishment.controller.dto.SnomedProblemDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/snomedproblems")
public class BackofficeSnomedProblemsController extends AbstractBackofficeController<SnomedProblemDto, Long> {

	public BackofficeSnomedProblemsController(BackofficeSnomedProblemsStore store)
	{
		super(store);
	}

}