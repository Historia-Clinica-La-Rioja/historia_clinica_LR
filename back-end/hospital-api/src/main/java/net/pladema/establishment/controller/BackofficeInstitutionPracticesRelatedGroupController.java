package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnomedRelatedGroupValidator;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;

@RestController
@RequestMapping("backoffice/institutionpracticesrelatedgroups")
public class BackofficeInstitutionPracticesRelatedGroupController extends BackofficeSnomedRelatedGroupController{

	public BackofficeInstitutionPracticesRelatedGroupController(SnomedRelatedGroupRepository repository, SnomedRelatedGroupRepository snomedRelatedGroupRepository, DateTimeProvider dateTimeProvider, BackofficeSnomedRelatedGroupValidator backofficeSnomedRelatedGroupValidator) {
		super(repository, snomedRelatedGroupRepository, dateTimeProvider, backofficeSnomedRelatedGroupValidator);
	}

}
