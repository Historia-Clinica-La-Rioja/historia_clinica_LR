package net.pladema.establishment.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnomedRelatedGroupValidator;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;

@RestController
@RequestMapping("backoffice/institutionpracticesrelatedgroups")
public class BackofficeInstitutionPracticesRelatedGroupController extends BackofficeSnomedRelatedGroupController{

	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	public BackofficeInstitutionPracticesRelatedGroupController(SnomedRelatedGroupRepository repository, SnomedRelatedGroupRepository snomedRelatedGroupRepository, DateTimeProvider dateTimeProvider, BackofficeSnomedRelatedGroupValidator backofficeSnomedRelatedGroupValidator) {
		super(repository, snomedRelatedGroupRepository, dateTimeProvider, backofficeSnomedRelatedGroupValidator);
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
	}

	@Override
	public SnomedRelatedGroup create(@Valid @RequestBody SnomedRelatedGroup entity) {
		Integer snomedId = snomedRelatedGroupRepository.getById(entity.getSnomedId()).getSnomedId();
		boolean hasPersisted = snomedRelatedGroupRepository.findBySnomedIdAndGroupId(snomedId, entity.getGroupId()).isPresent();
		if(hasPersisted)
			throw new BackofficeValidationException("La práctica ya fue agregada");
		return super.create(entity);
	}
}
