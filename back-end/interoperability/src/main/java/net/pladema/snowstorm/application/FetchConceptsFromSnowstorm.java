package net.pladema.snowstorm.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.snowstorm.domain.FullySpecifiedNamesBo;
import net.pladema.snowstorm.domain.PreferredTermBo;
import net.pladema.snowstorm.domain.SnomedSearchItemBo;
import net.pladema.snowstorm.services.SnowstormService;
import net.pladema.snowstorm.services.domain.SnowstormSearchResponse;
import net.pladema.snowstorm.services.exceptions.SnowstormApiException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class FetchConceptsFromSnowstorm {

	private final SnowstormService snowstormService;

	public List<SnomedSearchItemBo> run(String term, String eclKey) throws SnowstormApiException {
		log.debug("Search in snowstorm service...");
		SnowstormSearchResponse response = snowstormService.getConcepts(term, eclKey);
		return response.getItems().stream().map(s -> new SnomedSearchItemBo(s.getConceptId(),
						s.getConceptId(),
						new FullySpecifiedNamesBo(s.getFsn().get("term").textValue(), s.getFsn().get("lang").textValue()),
						new PreferredTermBo(s.getPt().get("term").textValue(), s.getFsn().get("lang").textValue())))
				.collect(Collectors.toList());
	}

}
