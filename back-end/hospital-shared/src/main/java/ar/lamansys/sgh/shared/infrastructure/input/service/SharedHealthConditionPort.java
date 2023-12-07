package ar.lamansys.sgh.shared.infrastructure.input.service;

public interface SharedHealthConditionPort {

	Integer getHealthConditionIdByEncounterAndSnomedConcept(Integer encounterId, Integer sourceTypeId, String sctid, String pt);

}
