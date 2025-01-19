package net.pladema.medicine.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicine.application.port.MedicineFinancingStatusStorage;
import net.pladema.medicine.infrastructure.output.repository.MedicineFinancingStatusRepository;

import net.pladema.medicine.infrastructure.output.repository.entity.MedicineFinancingStatus;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class MedicineFinancingStatusStorageImpl implements MedicineFinancingStatusStorage {

	private final MedicineFinancingStatusRepository medicineFinancingStatusRepository;

	@Override
	public List<Integer> addConcepts(List<Integer> conceptIds) {
		log.debug("Input parameters -> conceptIds size {}", conceptIds.size());
		Set<Integer> savedConcepts = medicineFinancingStatusRepository.findAll().stream().map(MedicineFinancingStatus::getId).collect(Collectors.toSet());
		List<MedicineFinancingStatus> conceptsToAdd = conceptIds.stream()
				.filter(id -> !savedConcepts.contains(id))
				.map(MedicineFinancingStatus::new)
				.collect(Collectors.toList());
		List<Integer> result = medicineFinancingStatusRepository.saveAll(conceptsToAdd).stream()
				.map(MedicineFinancingStatus::getId).collect(Collectors.toList());
		log.debug("Output -> result size {}", result.size());
		return result;
	}
}
