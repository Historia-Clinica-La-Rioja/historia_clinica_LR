package net.pladema.patient.application.port;

import java.util.List;

import net.pladema.person.controller.dto.BasicPersonalDataDto;

public interface MergePatientStorage {

	void inactivatePatient(Integer patientToInactivateId, Integer referencePatientId, Integer institutionId);

	void reactivatePatient(Integer patientIdToReactivate, Integer institutionId);

	void updatePersonByPatientId(Integer patientId, BasicPersonalDataDto dto, Integer institutionId);

	void saveMergeHistoricData(Integer activePatientId, List<Integer> inactivePatientIds);

	List<Integer> getInactivePatientsByActivePatientId(Integer patientId);

	void deleteMergeHistoricData(Integer patientId, List<Integer> inactivePatientIds);

	void assertBasicPersonData(BasicPersonalDataDto basicData);

}
