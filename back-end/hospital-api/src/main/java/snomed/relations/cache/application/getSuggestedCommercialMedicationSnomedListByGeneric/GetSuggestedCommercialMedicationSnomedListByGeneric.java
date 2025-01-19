package snomed.relations.cache.application.getSuggestedCommercialMedicationSnomedListByGeneric;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import snomed.relations.cache.application.ports.SnomedRelationCacheStorage;
import snomed.relations.cache.domain.SnomedBo;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetSuggestedCommercialMedicationSnomedListByGeneric {

	private final SnomedRelationCacheStorage snomedRelationCacheStorage;

	public List<SnomedBo> run(String genericMedicationSctid) {
		log.debug("Input parameters -> genericMedicationSctid {}", genericMedicationSctid);
		List<SnomedBo> result = snomedRelationCacheStorage.getSuggestedCommercialMedicationSnomedListByGeneric(genericMedicationSctid);
		log.debug("Output -> {}", result);
		return result;
	}

}
