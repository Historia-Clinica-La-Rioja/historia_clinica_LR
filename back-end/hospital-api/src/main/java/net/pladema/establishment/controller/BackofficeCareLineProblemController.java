package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.CareLineProblemDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/carelineproblems")
public class BackofficeCareLineProblemController extends AbstractBackofficeController <CareLineProblemDto, Integer> {

	public BackofficeCareLineProblemController(BackofficeCareLineProblemStore store){
		super(store);
	}

}
