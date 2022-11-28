package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.establishment.controller.dto.BackofficeSnowstormDto;

import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.VSnomedGroupConceptRepository;
import net.pladema.snowstorm.repository.entity.VSnomedGroupConcept;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backoffice/snowstormproblems")
public class BackofficeSnowstormProblemsController extends BackofficeSnomedGroupConceptsController {

	private final BackofficeSnowstormStore backofficeSnowstormStore;

	private final FeatureFlagsService featureFlagsService;

	private final SnomedGroupRepository snomedGroupRepository;

	public BackofficeSnowstormProblemsController(VSnomedGroupConceptRepository repository, BackofficeSnomedRelatedGroupController backofficeSnomedRelatedGroupController, FeatureFlagsService featureFlagsService, BackofficeSnowstormStore backofficeSnowstormStore, SnomedGroupRepository snomedGroupRepository) {
		super(repository, backofficeSnomedRelatedGroupController);
		this.backofficeSnowstormStore = backofficeSnowstormStore;
		this.featureFlagsService = featureFlagsService;
		this.snomedGroupRepository = snomedGroupRepository;
	}

	@Override
	public Page<VSnomedGroupConcept> getList(Pageable pageable, VSnomedGroupConcept entity) {
		if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
			Integer groupId = snomedGroupRepository.getIdByDescription(SnomedECL.DIAGNOSIS.name());
			entity.setGroupId(groupId);
			return super.getList(pageable, entity);
		}

		String conceptPt = entity.getConceptPt();
		if (conceptPt == null) return new PageImpl<>(Collections.emptyList());
		var apiConcepts = backofficeSnowstormStore.findAll(new BackofficeSnowstormDto(conceptPt), pageable, SnomedECL.DIAGNOSIS).getContent();
		if (apiConcepts.isEmpty()) return new PageImpl<>(Collections.emptyList());
		var result = apiConcepts.stream().filter(item -> (Long.parseLong(item.getConceptId()) <= Integer.MAX_VALUE)).map(this::mapToSnomedGroupConcept).collect(Collectors.toList());
		return new PageImpl<>(result, pageable, result.size());
	}

	@Override
	@GetMapping(params = "ids")
	public @ResponseBody
	Iterable<VSnomedGroupConcept> getMany(@RequestParam List<Integer> ids) {
		if(featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS))
			return super.getMany(ids);
		else
			return backofficeSnowstormStore.findAllById(ids).stream().map(this::mapToSnomedGroupConcept).collect(Collectors.toList());
	}

	private VSnomedGroupConcept mapToSnomedGroupConcept(BackofficeSnowstormDto dto) {
		return new VSnomedGroupConcept(Integer.parseInt(dto.getConceptId()), dto.getTerm());
	}

}