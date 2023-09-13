package net.pladema.establishment.controller;

import net.pladema.establishment.repository.RuleRepository;
import net.pladema.establishment.repository.entity.Rule;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/rules")
public class BackofficeRuleController extends AbstractBackofficeController<Rule, Integer> {

	public BackofficeRuleController(RuleRepository repository){
		super(new BackofficeRepository<Rule, Integer>(repository));
	}
}
