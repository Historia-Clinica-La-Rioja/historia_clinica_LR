package snomed.relations.cache.application.getMedicationPresentationUnits;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import snomed.relations.cache.application.ports.SnomedRelationCacheStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetMedicationPresentationUnits {

	private final SnomedRelationCacheStorage snomedRelationCacheStorage;

	public List<Integer> run(String medicationSctid) {
		log.debug("Input parameters -> medicationSctid {}", medicationSctid);
		List<Integer> result = snomedRelationCacheStorage.getMedicationPresentationUnits(medicationSctid);
		log.debug("Output -> {}", result);
		return result;
	}

}
