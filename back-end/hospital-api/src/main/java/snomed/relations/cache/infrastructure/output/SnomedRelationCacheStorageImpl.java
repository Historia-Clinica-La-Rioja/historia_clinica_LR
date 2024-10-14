package snomed.relations.cache.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import snomed.relations.cache.application.ports.SnomedRelationCacheStorage;
import snomed.relations.cache.domain.CommercialMedicationBo;
import snomed.relations.cache.domain.GetCommercialMedicationSnomedBo;
import snomed.relations.cache.domain.SnomedBo;
import snomed.relations.cache.infrastructure.output.repository.VCommercialMedicationRepository;
import snomed.relations.cache.infrastructure.output.repository.VMedicationPresentationUnitRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SnomedRelationCacheStorageImpl implements SnomedRelationCacheStorage {

	private final VCommercialMedicationRepository vCommercialMedicationRepository;

	private final VMedicationPresentationUnitRepository vMedicationPresentationUnitRepository;

	@Override
	public List<CommercialMedicationBo> getCommercialMedications() {
		List<CommercialMedicationBo> result = vCommercialMedicationRepository.getCommercialMedications();
		log.debug("Commercial presentations quantity -> {}", result.size());
		return result;
	}

	@Override
	public List<GetCommercialMedicationSnomedBo> getCommercialMedicationSnomedListByName(String commercialMedicationName) {
		int LIMIT = 30;
		Pageable pageable = PageRequest.of(0, LIMIT);
		return vCommercialMedicationRepository.fetchCommercialMedicationSnomedListByName(commercialMedicationName, pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Integer> getMedicationPresentationUnits(String commercialMedicationSctid) {
		return vMedicationPresentationUnitRepository.fetchMedicationPresentationUnits(commercialMedicationSctid)
				.map(this::parsePresentationUnitValue)
				.sorted(Integer::compareTo)
				.collect(Collectors.toList());
	}

	@Override
	public List<SnomedBo> getSuggestedCommercialMedicationSnomedListByGeneric(String genericMedicationSctid) {
		return vCommercialMedicationRepository.fetchSuggestedCommercialMedicationSnomedListByGeneric(genericMedicationSctid);
	}

	private Integer parsePresentationUnitValue(String presentationUnitValue) {
		final String PRESENTATION_UNIT_PREFIX = "#";
		final int PRESENTATION_UNIT_AMOUNT_INDEX = 1;
		return Integer.valueOf(presentationUnitValue.split(PRESENTATION_UNIT_PREFIX)[PRESENTATION_UNIT_AMOUNT_INDEX]);
	}

}
