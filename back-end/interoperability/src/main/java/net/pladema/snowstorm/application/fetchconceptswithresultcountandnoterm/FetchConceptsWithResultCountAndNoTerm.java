package net.pladema.snowstorm.application.fetchconceptswithresultcountandnoterm;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.application.FetchConceptsFromSnowstorm;
import net.pladema.snowstorm.domain.SnomedSearchBo;
import net.pladema.snowstorm.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;
import net.pladema.snowstorm.services.searchCachedConcepts.SearchCachedConceptsWithResultCount;

import net.pladema.snowstorm.services.searchCachedConcepts.SearchCachedConceptsWithResultCountAndNoTerm;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class FetchConceptsWithResultCountAndNoTerm {

	private final FeatureFlagsService featureFlagsService;

	private final FetchConceptsFromSnowstorm fetchConceptsFromSnowstorm;

	private final SearchCachedConceptsWithResultCountAndNoTerm searchCachedConceptsWithResultCountAndNoTerm;

	public SnomedSearchBo run(String eclKey) throws SnowstormApiException {
		log.debug("Input data -> ecl: {} ", eclKey);
		List<SnomedSearchItemBo> conceptsData;
		Integer conceptsQuantity;
		if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
			log.debug("Search locally...");
			var locallyConceptsWithResultCount = searchCachedConceptsWithResultCountAndNoTerm.run(eclKey);
			conceptsData = locallyConceptsWithResultCount.getItems();
			conceptsQuantity = locallyConceptsWithResultCount.getTotalMatches();
		} else {
			conceptsData = fetchConceptsFromSnowstorm.run("", eclKey);
			conceptsQuantity = conceptsData.size();
		}
		log.debug( "Output -> conceptsData: {}, conceptsQuantity: {}", conceptsData, conceptsQuantity);
		return new SnomedSearchBo(conceptsData, conceptsQuantity);
	}

}
