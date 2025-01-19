package net.pladema.establishment.controller;

import javax.validation.Valid;

import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.exceptions.BackofficeValidationException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;

import java.util.Optional;

@RestController
@RequestMapping("backoffice/institutionpracticesrelatedgroups")
public class BackofficeInstitutionPracticesRelatedGroupController extends AbstractBackofficeController<SnomedRelatedGroup, Integer>{

	private final FeatureFlagsService featureFlagsService;
	private final BackofficeSnowstormStore backofficeSnowstormStore;
	private final DateTimeProvider dateTimeProvider;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	public BackofficeInstitutionPracticesRelatedGroupController(FeatureFlagsService featureFlagsService,
																SnomedRelatedGroupRepository repository,
																BackofficeSnowstormStore backofficeSnowstormStore,
																DateTimeProvider dateTimeProvider,
																SnomedRelatedGroupRepository snomedRelatedGroupRepository) {
		super(new BackofficeRepository<>(repository));
		this.featureFlagsService = featureFlagsService;
		this.backofficeSnowstormStore = backofficeSnowstormStore;
		this.dateTimeProvider = dateTimeProvider;
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
	}

	@Override
	public SnomedRelatedGroup create(@Valid @RequestBody SnomedRelatedGroup entity) {
		Integer snomedId;
		if(!featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
			snomedId = backofficeSnowstormStore.saveSnowstormConcept(entity.getSnomedId().toString());
		} else {
			snomedId = snomedRelatedGroupRepository.getById(entity.getSnomedId()).getSnomedId();
		}
		Integer groupId = entity.getGroupId();
		Integer orden = snomedRelatedGroupRepository.getLastOrdenByGroupId(entity.getGroupId()).orElse(0) + 1;
		Optional<SnomedRelatedGroup> snomedRelatedGroup = snomedRelatedGroupRepository.getByGroupIdAndSnomedId(groupId, snomedId);
		if (snomedRelatedGroup.isPresent()){
			throw new BackofficeValidationException("La practica ya se encuentra asociada al grupo de practicas.");
		}
		return snomedRelatedGroupRepository.save(new SnomedRelatedGroup(snomedId, groupId, orden, dateTimeProvider.nowDate()));
	}

}
