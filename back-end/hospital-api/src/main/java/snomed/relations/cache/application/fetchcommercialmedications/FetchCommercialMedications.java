package snomed.relations.cache.application.fetchcommercialmedications;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import snomed.relations.cache.application.ports.SnomedRelationCacheStorage;
import snomed.relations.cache.domain.CommercialMedicationBo;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FetchCommercialMedications {

	private final SnomedRelationCacheStorage snomedRelationCacheStorage;

	public List<CommercialMedicationBo> run() {
		return snomedRelationCacheStorage.getCommercialMedications();
	}

}
