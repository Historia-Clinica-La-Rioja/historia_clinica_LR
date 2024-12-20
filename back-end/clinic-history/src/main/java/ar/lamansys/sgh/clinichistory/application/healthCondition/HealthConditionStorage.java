package ar.lamansys.sgh.clinichistory.application.healthCondition;

import java.util.List;
import java.util.Optional;

public interface HealthConditionStorage {
	List<Integer> getHealthConditionIdByEncounterAndSnomedConcept(Integer encounterId, Integer sourceTypeId, String sctid, String pt);

	List<Integer> getHealthConditionIdByDocumentIdAndSnomedConcept(Long documentId, String sctid, String pt);
}
