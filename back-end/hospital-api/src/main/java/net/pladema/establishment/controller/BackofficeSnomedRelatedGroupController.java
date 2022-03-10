package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
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
												  DateTimeProvider dateTimeProvider) {
        super(repository);
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
		this.dateTimeProvider = dateTimeProvider;
	}

	@Override
	public SnomedRelatedGroup create(@Valid @RequestBody SnomedRelatedGroup entity) {
		Integer orden = snomedRelatedGroupRepository.getLastOrdenByGroupId(entity.getGroupId()).orElse(0) + 1;
		entity.setOrden(orden);
		entity.setLastUpdate(dateTimeProvider.nowDate());
		return super.create(entity);
	}

}
