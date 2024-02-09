package net.pladema.snowstorm.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.cipres.domain.SnomedBo;
import net.pladema.snowstorm.repository.SnomedGroupRepository;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class GetSnomedConceptEclRelated {

	private SnomedGroupRepository snomedGroupRepository;

	public List<SnomedBo> run(List<SnomedBo> concepts, SnomedECL ecl) {
		log.debug("Input parameters -> concepts {}, ecl {}", concepts, ecl);
		List<SnomedBo> result = concepts.stream().filter(concept -> conceptIsRelatedWithEcl(concept, ecl)).collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

	private boolean conceptIsRelatedWithEcl(SnomedBo concept, SnomedECL ecl) {
		Integer isRelated = snomedGroupRepository.isEclRelatedBySnomedConceptIdAndTerm(concept.getSctid(), concept.getPt(), ecl.name());
		return isRelated != null;
	}

}
