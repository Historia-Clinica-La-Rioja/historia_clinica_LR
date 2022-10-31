package net.pladema.establishment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnomedRelatedGroupValidator;
import net.pladema.establishment.repository.CareLineInstitutionPracticeRepository;
import net.pladema.establishment.repository.entity.CareLineInstitutionPractice;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;

@RestController
@RequestMapping("backoffice/snomedrelatedgroups")
public class BackofficeSnomedRelatedGroupController extends AbstractBackofficeController<SnomedRelatedGroup, Integer>{

	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	private final CareLineInstitutionPracticeRepository careLineInstitutionPracticeRepository;

	private final DateTimeProvider dateTimeProvider;

    public BackofficeSnomedRelatedGroupController(SnomedRelatedGroupRepository repository,
												  SnomedRelatedGroupRepository snomedRelatedGroupRepository,
												  DateTimeProvider dateTimeProvider,
												  BackofficeSnomedRelatedGroupValidator backofficeSnomedRelatedGroupValidator,
												  CareLineInstitutionPracticeRepository careLineInstitutionPracticeRepository) {
        super(repository, backofficeSnomedRelatedGroupValidator);
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
		this.dateTimeProvider = dateTimeProvider;
		this.careLineInstitutionPracticeRepository = careLineInstitutionPracticeRepository;
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

	@Override
	public BackofficeDeleteResponse<Integer> delete(Integer id) {
		Integer entityId = snomedRelatedGroupRepository.getById(id).getId();
		List<CareLineInstitutionPractice> persistedEntitiesInAdherence = careLineInstitutionPracticeRepository.findBySnomedRelatedGroupId(entityId);
		if(!persistedEntitiesInAdherence.isEmpty())
			throw new BackofficeValidationException(String.format("La práctica está asociada a %s líneas de cuidado", persistedEntitiesInAdherence.size()));
		return super.delete(id);
	}
}
