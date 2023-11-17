package net.pladema.establishment.controller;

import javax.validation.Valid;

import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnomedRelatedGroupValidator;
import net.pladema.establishment.controller.dto.BackofficeSnowstormDto;
import net.pladema.establishment.repository.CareLineInstitutionPracticeRepository;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;

@RestController
@RequestMapping("backoffice/institutionpracticesrelatedgroups")
public class BackofficeInstitutionPracticesRelatedGroupController extends AbstractBackofficeController<SnomedRelatedGroup, Integer>{

	private final FeatureFlagsService featureFlagsService;
	private final BackofficeSnowstormStore backofficeSnowstormStore;
	private final BackofficeSnomedRelatedGroupController backofficeSnomedRelatedGroupController;

	public BackofficeInstitutionPracticesRelatedGroupController(FeatureFlagsService featureFlagsService,
																SnomedRelatedGroupRepository repository,
																BackofficeSnowstormStore backofficeSnowstormStore,
																BackofficeSnomedRelatedGroupController backofficeSnomedRelatedGroupController) {
		super(new BackofficeRepository<>(repository));
		this.featureFlagsService = featureFlagsService;
		this.backofficeSnowstormStore = backofficeSnowstormStore;
		this.backofficeSnomedRelatedGroupController = backofficeSnomedRelatedGroupController;
	}

	@Override
	public SnomedRelatedGroup create(@Valid @RequestBody SnomedRelatedGroup entity) {
		if(featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS))
			return backofficeSnomedRelatedGroupController.create(entity);
		else try{
			var snomedId = backofficeSnowstormStore.saveSnowstormConcept(entity.getSnomedId().toString(), entity.getGroupId(), null);
			entity.setSnomedId(snomedId);
			return backofficeSnomedRelatedGroupController.create(entity);
		} catch (SnowstormPortException e) {
			throw new RuntimeException(e);
		}
	}
}
