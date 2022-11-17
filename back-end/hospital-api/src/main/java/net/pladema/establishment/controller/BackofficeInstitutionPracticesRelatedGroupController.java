package net.pladema.establishment.controller;

import javax.validation.Valid;

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
public class BackofficeInstitutionPracticesRelatedGroupController extends BackofficeSnomedRelatedGroupController{

	private final FeatureFlagsService featureFlagsService;

	private final SnomedService snomedService;

	private final BackofficeSnowstormStore backofficeSnowstormStore;

	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;

	private final DateTimeProvider dateTimeProvider;

	public BackofficeInstitutionPracticesRelatedGroupController(SnomedRelatedGroupRepository repository,
																SnomedRelatedGroupRepository snomedRelatedGroupRepository,
																DateTimeProvider dateTimeProvider,
																BackofficeSnomedRelatedGroupValidator backofficeSnomedRelatedGroupValidator,
																FeatureFlagsService featureFlagsService,
																SnomedService snomedService,
																BackofficeSnowstormStore backofficeSnowstormStore,
																CareLineInstitutionPracticeRepository careLineInstitutionPracticeRepository) {
		super(repository, snomedRelatedGroupRepository, dateTimeProvider, backofficeSnomedRelatedGroupValidator, careLineInstitutionPracticeRepository);
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
		this.featureFlagsService = featureFlagsService;
		this.snomedService = snomedService;
		this.backofficeSnowstormStore = backofficeSnowstormStore;
		this.dateTimeProvider = dateTimeProvider;
	}

	@Override
	public SnomedRelatedGroup create(@Valid @RequestBody SnomedRelatedGroup entity) {
		if(featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS))
			return super.create(entity);

		BackofficeSnowstormDto concept = backofficeSnowstormStore.findById(entity.getSnomedId()).orElse(null);
		if(concept != null) {
			var snomedBo = new SnomedBo(concept.getConceptId(), concept.getTerm());
			Integer snomedId = snomedService.getSnomedId(snomedBo).orElse(null);
			if (snomedId == null)
				snomedId = snomedService.createSnomedTerm(snomedBo);

			Integer orden = snomedRelatedGroupRepository.getLastOrdenByGroupId(entity.getGroupId()).orElse(0) + 1;
			entity.setOrden(orden);
			entity.setLastUpdate(dateTimeProvider.nowDate());
			entity.setSnomedId(snomedId);

			return snomedRelatedGroupRepository.save(entity);
		}

		return null;
	}
}
