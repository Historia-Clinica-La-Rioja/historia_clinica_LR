package net.pladema.patient.application.port;

import net.pladema.person.controller.dto.BasicPersonalDataDto;

import java.util.List;

public interface MergePatientStorage {

	Boolean existPatientById(Integer id);

	void inactivatePatient(Integer patientToInactivateId, Integer referencePatientId, Integer institutionId);

	void updatePersonByPatientId(Integer patientId, BasicPersonalDataDto dto, Integer institutionId);

	void saveMergeHistoricData(Integer activePatientId, List<Integer> inactivePatientIds);

}
