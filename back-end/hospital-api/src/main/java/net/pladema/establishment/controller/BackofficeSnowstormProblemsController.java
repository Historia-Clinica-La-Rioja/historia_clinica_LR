package net.pladema.establishment.controller;

import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;


import net.pladema.establishment.controller.dto.BackofficeSnowstormDto;

import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.snowstorm.services.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import net.pladema.snowstorm.services.searchCachedConcepts.SearchCachedConcepts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backoffice/snowstormproblems")
public class BackofficeSnowstormProblemsController extends AbstractBackofficeController<BackofficeSnowstormDto, Long> {

	private final BackofficeSnowstormStore backofficeSnowstormStore;

	private final SearchCachedConcepts searchCachedConcepts;

	private final SnomedService snomedService;

	private final FeatureFlagsService featureFlagsService;


	public BackofficeSnowstormProblemsController(BackofficeSnowstormStore backofficeSnowstormStore,
												 SearchCachedConcepts searchCachedConcepts,
												 SnomedService snomedService,
												 FeatureFlagsService featureFlagsService)
	{
		super(backofficeSnowstormStore);
		this.backofficeSnowstormStore = backofficeSnowstormStore;
		this.snomedService = snomedService;
		this.searchCachedConcepts = searchCachedConcepts;
		this.featureFlagsService = featureFlagsService;
	}

	@Override
	public Page<BackofficeSnowstormDto> getList(Pageable pageable, BackofficeSnowstormDto entity) {
		if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
			List<BackofficeSnowstormDto> content = searchCachedConcepts.run(entity.getTerm(), SnomedECL.DIAGNOSIS.toString())
					.getItems()
					.stream()
					.map(this::mapToBackofficeSnowstormDto)
					.collect(Collectors.toList());
			int maxIndex = pageable.getPageSize() < content.size() ? (pageable.getPageSize()) : (content.size() == 0 ? 0 : content.size());
			return new PageImpl<>(content.subList(0, maxIndex), pageable, content.size());
		}
		return backofficeSnowstormStore.findAll(entity, pageable, SnomedECL.DIAGNOSIS);
	}

	@Override
	@GetMapping(params = "ids")
	public @ResponseBody
	Iterable<BackofficeSnowstormDto> getMany(@RequestParam List<Long> ids) {
		if (!ids.isEmpty()){
			if(featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
				Long conceptId = ids.get(0);
				var concept = snomedService.getSnomed(conceptId.intValue());
				List<BackofficeSnowstormDto> result = new ArrayList<>();
				result.add(new BackofficeSnowstormDto(conceptId, concept.getSctid(), concept.getPt()));
				return result;
			} else
				return backofficeSnowstormStore.findAllById(ids);
			}
		return Collections.emptyList();
	}

	private BackofficeSnowstormDto mapToBackofficeSnowstormDto(SnomedSearchItemBo concept) {
		return new BackofficeSnowstormDto(Long.valueOf(concept.getSnomedId()), concept.getSctid(), concept.getPt());
	}

}