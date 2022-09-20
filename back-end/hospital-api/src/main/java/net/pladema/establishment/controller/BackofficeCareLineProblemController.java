package net.pladema.establishment.controller;

import net.pladema.establishment.repository.entity.CareLineProblem;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/carelineproblems")
public class BackofficeCareLineProblemController extends AbstractBackofficeController <CareLineProblem, Integer> {

	public BackofficeCareLineProblemController(BackofficeCareLineProblemStore store){
		super(store);
	}

}
