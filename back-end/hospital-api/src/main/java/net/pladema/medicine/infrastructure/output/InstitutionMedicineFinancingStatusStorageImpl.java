package net.pladema.medicine.infrastructure.output;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.InstitutionRepository;
import net.pladema.medicine.application.port.InstitutionMedicineFinancingStatusStorage;

import net.pladema.medicine.infrastructure.output.repository.InstitutionMedicineFinancingStatusRepository;

import net.pladema.medicine.infrastructure.output.repository.entity.InstitutionMedicineFinancingStatus;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class InstitutionMedicineFinancingStatusStorageImpl implements InstitutionMedicineFinancingStatusStorage {

	private final InstitutionMedicineFinancingStatusRepository institutionMedicineFinancingStatusRepository;
	private final InstitutionRepository institutionRepository;

	@Override
	public void addConceptsToAllInstitutions(List<Integer> conceptsIds){
		log.debug("Input parameters -> conceptsIds {}", conceptsIds);
		List<Integer> institutionsIds = institutionRepository.getAllIds();
		List<InstitutionMedicineFinancingStatus> conceptsToAdd = new ArrayList<>();
		for (Integer conceptId: conceptsIds){
			institutionsIds.forEach(institutionId -> conceptsToAdd.add(new InstitutionMedicineFinancingStatus(institutionId, conceptId)));
		}
		institutionMedicineFinancingStatusRepository.saveAll(conceptsToAdd);
	}
}
