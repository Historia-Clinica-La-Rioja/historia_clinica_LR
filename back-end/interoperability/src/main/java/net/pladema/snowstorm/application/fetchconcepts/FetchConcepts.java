package net.pladema.snowstorm.application.fetchconcepts;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.application.FetchConceptsFromSnowstorm;
import net.pladema.snowstorm.domain.FullySpecifiedNamesBo;
import net.pladema.snowstorm.domain.PreferredTermBo;
import net.pladema.snowstorm.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;

import net.pladema.snowstorm.services.searchCachedConcepts.SearchCachedConcepts;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class FetchConcepts {

	private static final String OUTPUT = "Output -> {}";

	private final FeatureFlagsService featureFlagsService;

	private final FetchConceptsFromSnowstorm fetchConceptsFromSnowstorm;

	private final SearchCachedConcepts searchCachedConcepts;


	public List<SnomedSearchItemBo> run(String term, String eclKey) throws SnowstormApiException {
		log.debug("Input data -> term: {} , ecl: {} ", term, eclKey);
		List<SnomedSearchItemBo> result;
		if (featureFlagsService.isOn(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)) {
			log.debug("Search locally...");
			result = searchCachedConcepts.run(term, eclKey);
		} else
			result = fetchConceptsFromSnowstorm.run(term, eclKey);
		log.trace(OUTPUT, result);
		return result;
	}

}
