package ar.lamansys.sgh.shared.infrastructure.input.service;

import java.util.Optional;

public interface SharedHealthConditionPort {

	Integer getHealthConditionIdByEncounterAndSnomedConcept(Integer encounterId, Integer sourceTypeId, String sctid, String pt);

	Optional<Integer> getLatestHealthConditionByPatientIdAndInstitutionIdAndSnomedConcept(
		Integer institutionId,
		Integer patientId,
		String sctid,
		String pt
	);

}
