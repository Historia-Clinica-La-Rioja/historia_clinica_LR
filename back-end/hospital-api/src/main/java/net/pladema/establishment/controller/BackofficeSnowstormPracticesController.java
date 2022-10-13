package net.pladema.establishment.controller;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.establishment.controller.dto.BackofficeSnowstormDto;
import net.pladema.snowstorm.repository.VSnomedGroupConceptRepository;
import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

@RestController
@RequestMapping("backoffice/snowstormpractices")
public class BackofficeSnowstormPracticesController extends BackofficeSnomedGroupConceptsController{

	private final FeatureFlagsService featureFlagsService;

	private final BackofficeSnowstormStore backofficeSnowstormStore;

	public BackofficeSnowstormPracticesController(VSnomedGroupConceptRepository repository, BackofficeSnomedRelatedGroupController backofficeSnomedRelatedGroupController,
												  FeatureFlagsService featureFlagsService, BackofficeSnowstormStore backofficeSnowstormStore) {
		super(repository, backofficeSnomedRelatedGroupController);
		this.featureFlagsService = featureFlagsService;
		this.backofficeSnowstormStore = backofficeSnowstormStore;
	}

	@Override
	public Page<VSnomedGroupConcept> getList(Pageable pageable, VSnomedGroupConcept entity) {
		if(featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS))
			return super.getList(pageable, entity);

		var apiConcepts = backofficeSnowstormStore.findAll(
				new BackofficeSnowstormDto(entity.getConceptPt()), pageable, SnomedECL.PROCEDURE)
				.stream()
				.map(this::mapToSnomedGroupConcept)
				.collect(Collectors.toList());
		return new PageImpl<>(apiConcepts, pageable, apiConcepts.size());
	}

	private VSnomedGroupConcept mapToSnomedGroupConcept(BackofficeSnowstormDto dto) {
		return new VSnomedGroupConcept(Integer.parseInt(dto.getConceptId()), dto.getTerm());
	}
}
