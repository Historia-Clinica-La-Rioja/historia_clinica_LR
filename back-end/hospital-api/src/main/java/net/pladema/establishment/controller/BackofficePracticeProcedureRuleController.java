package net.pladema.establishment.controller;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/practiceprocedurerules")
public class BackofficePracticeProcedureRuleController extends AbstractBackofficeController<VSnomedGroupConcept, Integer> {

	public BackofficePracticeProcedureRuleController(BackofficePracticeProcedureRuleStore store){
		super(store);
	}
}
