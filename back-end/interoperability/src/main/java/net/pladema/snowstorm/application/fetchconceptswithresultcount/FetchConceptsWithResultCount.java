package net.pladema.snowstorm.application.fetchconceptswithresultcount;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.application.FetchConceptsFromSnowstorm;
import net.pladema.snowstorm.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.domain.SnomedSearchBo;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;

import net.pladema.snowstorm.services.searchCachedConcepts.SearchCachedConceptsWithResultCount;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FetchConceptsWithResultCount {

	private final FeatureFlagsService featureFlagsService;

	private final FetchConceptsFromSnowstorm fetchConceptsFromSnowstorm;

	private final SearchCachedConceptsWithResultCount searchCachedConceptsWithResultCount;

	public SnomedSearchBo run(String term, String eclKey) throws SnowstormApiException {
		log.debug("Input data -> term: {} , ecl: {} ", term, eclKey);
		List<SnomedSearchItemBo> conceptsData;
		Integer conceptsQuantity;
		if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
			log.debug("Search locally...");
			var locallyConceptsWithResultCount = searchCachedConceptsWithResultCount.run(term, eclKey);
			conceptsData = locallyConceptsWithResultCount.getItems();
			conceptsQuantity = locallyConceptsWithResultCount.getTotalMatches();
		} else {
			conceptsData = fetchConceptsFromSnowstorm.run(term, eclKey);
			conceptsQuantity = conceptsData.size();
		}
		log.debug( "Output -> conceptsData: {}, conceptsQuantity: {}", conceptsData, conceptsQuantity);
		return new SnomedSearchBo(conceptsData, conceptsQuantity);
	}

}
