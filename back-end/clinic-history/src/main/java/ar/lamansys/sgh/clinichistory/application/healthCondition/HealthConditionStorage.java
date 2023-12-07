package ar.lamansys.sgh.clinichistory.application.healthCondition;

import java.util.List;

public interface HealthConditionStorage {
	List<Integer> getHealthConditionIdByEncounterAndSnomedConcept(Integer encounterId, Integer sourceTypeId, String sctid, String pt);
}
