package net.pladema.medicine.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicine.application.port.InstitutionMedicineFinancingStatusStorage;
import net.pladema.medicine.application.port.MedicineFinancingStatusStorage;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;

import net.pladema.snowstorm.repository.entity.SnomedGroup;
import net.pladema.snowstorm.repository.entity.SnomedRelatedGroup;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Aspect
@Service
public class UpdateMedicineFinancingStatusConcepts {

	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;
	private final SnomedGroupRepository snomedGroupRepository;
	private final MedicineFinancingStatusStorage medicineFinancingStatusStorage;
	private final SnomedSemantics snomedSemantics;
	private final InstitutionMedicineFinancingStatusStorage institutionMedicineFinancingStatusStorage;

	@After("execution(* net.pladema.snowstorm.services.loadCsv.UpdateSnomedConceptsByCsv.updateSnomedConcepts(..)) || " +
			"execution(* net.pladema.snowstorm.services.loadCsv.UpdateSnomedConceptsByCsv.updateSnomedConceptSynonyms(..))")
	@Transactional
	public List<Integer> run (){
		log.warn("Updating concepts on MedicineFinancingStatus");
		List<Integer> conceptsAdded = new ArrayList<>();
		String medicineEcl = snomedSemantics.getEcl(SnomedECL.MEDICINE);
		Integer groupId = snomedGroupRepository.getBaseGroupIdByEclAndDescription(medicineEcl, SnomedECL.MEDICINE.toString());
		LocalDate groupLastUpdate = snomedGroupRepository.findById(groupId).map(SnomedGroup::getLastUpdate).orElse(null);
		if (groupLastUpdate != null && groupLastUpdate.isAfter(LocalDate.now().minusDays(1))){
			List<Integer> conceptIds = snomedRelatedGroupRepository.getConceptsIdsByGroupId(groupId);
			conceptsAdded = medicineFinancingStatusStorage.addConcepts(conceptIds);
			institutionMedicineFinancingStatusStorage.addConceptsToAllInstitutions(conceptsAdded);
		}
		log.warn("Concepts added -> {}", conceptsAdded.size());
		return conceptsAdded;
	}

}
