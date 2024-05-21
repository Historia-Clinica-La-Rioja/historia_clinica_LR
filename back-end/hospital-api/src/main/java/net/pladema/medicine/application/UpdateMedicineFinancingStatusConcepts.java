package net.pladema.medicine.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.medicine.application.port.MedicineFinancingStatusStorage;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;

import net.pladema.snowstorm.services.domain.semantics.SnomedECL;

import net.pladema.snowstorm.services.domain.semantics.SnomedSemantics;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

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

	@After("execution(* net.pladema.snowstorm.services.loadCsv.UpdateSnomedConceptsByCsv.updateSnomedConcepts(..)) || " +
			"execution(* net.pladema.snowstorm.services.loadCsv.UpdateSnomedConceptsByCsv.updateSnomedConceptSynonyms(..))")
	public List<Integer> run (){
		log.warn("Updating concepts on MedicineFinancingStatus");
		String medicineEcl = snomedSemantics.getEcl(SnomedECL.MEDICINE);
		Integer groupId = snomedGroupRepository.getBaseGroupIdByEclAndDescription(medicineEcl, SnomedECL.MEDICINE.toString());
		List<Integer> conceptIds = snomedRelatedGroupRepository.getConceptsIdsByGroupId(groupId);
		List<Integer> result = medicineFinancingStatusStorage.addConcepts(conceptIds);
		log.warn("Concepts added -> {}", result.size());
		return result;
	}

}
