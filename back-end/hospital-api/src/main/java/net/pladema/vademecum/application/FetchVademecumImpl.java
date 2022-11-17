package net.pladema.vademecum.application;

import net.pladema.vademecum.domain.SnomedBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchVademecumImpl implements FetchVademecum {

	private final Logger logger;

	private final VademecumStorage vademecumStorage;

	public FetchVademecumImpl(VademecumStorage vademecumStorage) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.vademecumStorage = vademecumStorage;
	}

	@Override
	public List<SnomedBo> getConcepts(String term, String ecl, Integer institutionId) {
		logger.debug("Input -> {}", term, ecl, institutionId);
		List<SnomedBo> concepts = this.vademecumStorage.searchConcepts(term, ecl, institutionId);
		logger.debug("Output -> {}", concepts);
		return concepts;
	}

	@Override
	public Long getTotalConcepts(String term, String ecl, Integer institutionId) {
		logger.debug("Input -> {}", term, ecl, institutionId);
		Long total = this.vademecumStorage.getTotalConcepts(term, ecl, institutionId);
		logger.debug("Output -> {}", total);
		return total;
	}


}
