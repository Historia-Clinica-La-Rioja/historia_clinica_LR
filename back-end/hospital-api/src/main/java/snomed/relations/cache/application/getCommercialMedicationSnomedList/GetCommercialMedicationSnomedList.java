package snomed.relations.cache.application.getCommercialMedicationSnomedList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import snomed.relations.cache.application.ports.SnomedRelationCacheStorage;
import snomed.relations.cache.domain.GetCommercialMedicationSnomedBo;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetCommercialMedicationSnomedList {

	private final SnomedRelationCacheStorage snomedRelationCacheStorage;

	public List<GetCommercialMedicationSnomedBo> run(String commercialMedicationName) {
		log.debug("Input parameter -> commercialMedicationName {}", commercialMedicationName);
		List<GetCommercialMedicationSnomedBo> result = snomedRelationCacheStorage.getCommercialMedicationSnomedListByName(commercialMedicationName);
		log.debug("Output -> {}", result);
		return result;
	}

}
