package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.SnomedProcedureDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/snomedprocedurerules")
public class BackofficeSnomedProcedureRuleController extends AbstractBackofficeController<SnomedProcedureDto, Long> {

	public BackofficeSnomedProcedureRuleController(BackofficeSnomedProcedureRuleStore store){
		super(store);
	}
}
