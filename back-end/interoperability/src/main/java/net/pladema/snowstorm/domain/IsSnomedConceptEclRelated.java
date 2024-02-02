package net.pladema.snowstorm.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.cipres.domain.SnomedBo;
import net.pladema.snowstorm.repository.SnomedGroupRepository;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class IsSnomedConceptEclRelated {

	private SnomedGroupRepository snomedGroupRepository;

	public Boolean run(List<SnomedBo> concepts, SnomedECL ecl) {
		log.debug("Input parameters -> concepts {}, ecl {}", concepts, ecl);
		boolean result = conceptIsRelatedWithEcl(concepts, ecl);
		log.debug("Output -> {}", result);
		return result;
	}

	private boolean conceptIsRelatedWithEcl(List<SnomedBo> concepts, SnomedECL ecl) {
		boolean result = false;
		Iterator<SnomedBo> iterator = concepts.iterator();
		while (iterator.hasNext() && !result)
			result = checkEclConceptRelation(ecl, iterator, result);
		return result;
	}

	private boolean checkEclConceptRelation(SnomedECL ecl, Iterator<SnomedBo> iterator, boolean result) {
		SnomedBo concept = iterator.next();
		Integer isRelated = snomedGroupRepository.isEclRelatedBySnomedConceptIdAndTerm(concept.getSctid(), concept.getPt(), ecl.name());
		if (isRelated != null)
			result = true;
		return result;
	}

}
