package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnomedRelatedGroupValidator;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("backoffice/snomedrelatedgroups")
public class BackofficeSnomedRelatedGroupController extends AbstractBackofficeController<SnomedRelatedGroup, Integer>{

	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	private final DateTimeProvider dateTimeProvider;

    public BackofficeSnomedRelatedGroupController(SnomedRelatedGroupRepository repository,
												  SnomedRelatedGroupRepository snomedRelatedGroupRepository,
												  DateTimeProvider dateTimeProvider,
												  BackofficeSnomedRelatedGroupValidator backofficeSnomedRelatedGroupValidator) {
        super(repository, backofficeSnomedRelatedGroupValidator);
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
		this.dateTimeProvider = dateTimeProvider;
	}

	@Override
	public SnomedRelatedGroup create(@Valid @RequestBody SnomedRelatedGroup entity) {
		// as we get the SnomedRelatedGroup id in the snomedId field of the entity, we need to get the real snomedId
		Integer snomedId = snomedRelatedGroupRepository.getById(entity.getSnomedId()).getSnomedId();
		Integer orden = snomedRelatedGroupRepository.getLastOrdenByGroupId(entity.getGroupId()).orElse(0) + 1;
		entity.setOrden(orden);
		entity.setLastUpdate(dateTimeProvider.nowDate());
		entity.setSnomedId(snomedId);
		return super.create(entity);
	}

}
