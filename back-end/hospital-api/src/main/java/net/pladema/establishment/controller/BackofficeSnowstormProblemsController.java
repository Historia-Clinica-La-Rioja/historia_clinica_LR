package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.establishment.controller.dto.BackofficeSnowstormDto;

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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backoffice/snowstormproblems")
public class BackofficeSnowstormProblemsController extends BackofficeSnomedGroupConceptsController {

	private final BackofficeSnowstormStore backofficeSnowstormStore;

	private final FeatureFlagsService featureFlagsService;

	private final Integer DIAGNOSIS_GROUP_ID = 11;

	public BackofficeSnowstormProblemsController(VSnomedGroupConceptRepository repository,
												 BackofficeSnomedRelatedGroupController backofficeSnomedRelatedGroupController,
												 FeatureFlagsService featureFlagsService,
												 BackofficeSnowstormStore backofficeSnowstormStore)
	{
		super(repository, backofficeSnomedRelatedGroupController);
		this.backofficeSnowstormStore = backofficeSnowstormStore;
		this.featureFlagsService = featureFlagsService;
	}

	@Override
	public Page<VSnomedGroupConcept> getList(Pageable pageable, VSnomedGroupConcept entity) {
		List<VSnomedGroupConcept> concepts;
		if(featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)){
			entity.setGroupId(DIAGNOSIS_GROUP_ID);
			concepts = super.getList(pageable, entity)
					.stream()
					.sorted(Comparator.comparing(VSnomedGroupConcept::getConceptPt,
							Comparator.nullsFirst(Comparator.naturalOrder())))
					.collect(Collectors.toList());
		} else {
			concepts = backofficeSnowstormStore.findAll(new BackofficeSnowstormDto(entity.getConceptPt()), pageable, SnomedECL.DIAGNOSIS)
					.stream()
					.map(this::mapToSnomedGroupConcept)
					.sorted(Comparator.comparing(VSnomedGroupConcept::getConceptPt,
							Comparator.nullsFirst(Comparator.naturalOrder())))
					.collect(Collectors.toList());
		}
		return new PageImpl<>(concepts, pageable, concepts.size());
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
