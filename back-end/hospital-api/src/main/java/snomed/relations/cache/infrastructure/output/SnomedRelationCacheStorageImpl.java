package snomed.relations.cache.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import snomed.relations.cache.application.ports.SnomedRelationCacheStorage;
import snomed.relations.cache.domain.CommercialMedicationBo;
import snomed.relations.cache.infrastructure.output.repository.VCommercialMedicationRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SnomedRelationCacheStorageImpl implements SnomedRelationCacheStorage {

	private final VCommercialMedicationRepository vCommercialMedicationRepository;

	@Override
	public List<CommercialMedicationBo> getCommercialMedications() {
		List<CommercialMedicationBo> result = vCommercialMedicationRepository.getCommercialMedications();
		log.debug("Commercial presentations quantity -> {}", result.size());
		return result;
	}
}
