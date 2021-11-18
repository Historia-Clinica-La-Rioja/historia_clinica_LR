package ar.lamansys.sgh.clinichistory.application.healthCondition;

public interface HealthConditionStorage {
    Integer getHealthConditionIdByEncounterAndSnomedConcept(Integer encounterId, Integer sourceTypeId, String sctid, String pt);
}
