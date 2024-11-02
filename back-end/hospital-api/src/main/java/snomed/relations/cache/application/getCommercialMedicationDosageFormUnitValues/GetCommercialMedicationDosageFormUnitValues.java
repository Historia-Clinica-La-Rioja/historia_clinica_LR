package snomed.relations.cache.application.getCommercialMedicationDosageFormUnitValues;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import snomed.relations.cache.application.ports.SnomedRelationCacheStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetCommercialMedicationDosageFormUnitValues {

	private final SnomedRelationCacheStorage snomedRelationCacheStorage;

	public List<String> run(String genericSctid) {
		log.debug("Input parameters -> sctid {}", genericSctid);
		List<String> result = snomedRelationCacheStorage.getAllGenericPresentationUnitUnitValues(genericSctid);
		log.debug("Output -> {}", result);
		return result;
	}

}
