package net.pladema.patient.application.mergepatient;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.application.port.MergePatientStorage;
import net.pladema.patient.controller.dto.PatientToMergeDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MergePatient {

	private final MergePatientStorage mergePatientStorage;

	private final MigrateClinicHistory migrateClinicHistory;

	public Integer run(Integer institutionId, PatientToMergeDto patientToMerge) {
		log.debug("Input parameters -> institutionId {}, patientToMerge {}", institutionId, patientToMerge);
		mergePatientStorage.assertBasicPersonData(patientToMerge.getRegistrationDataPerson());
		Integer activePatientId = patientToMerge.getActivePatientId();
		patientToMerge.getOldPatientsIds().forEach(id -> mergePatientStorage.inactivatePatient(id, activePatientId, institutionId));
		mergePatientStorage.updatePersonByPatientId(activePatientId, patientToMerge.getRegistrationDataPerson(), institutionId);
		mergePatientStorage.saveMergeHistoricData(activePatientId,patientToMerge.getOldPatientsIds());

		migrateClinicHistory.execute(patientToMerge.getOldPatientsIds(), patientToMerge.getActivePatientId());
		migratePatientData(patientToMerge.getOldPatientsIds(), patientToMerge.getActivePatientId());
		
		log.debug("Output result -> {}", activePatientId);
		return activePatientId;
	}

	private void migratePatientData(List<Integer> oldPatients, Integer newPatient) {
		log.debug("Input parameters -> oldPatients{}, newPatient{}",oldPatients,newPatient);

		mergePatientStorage.modifyAdditionalDoctor(oldPatients, newPatient);
		mergePatientStorage.modifyPatientMedicalCoverage(oldPatients, newPatient);
	}

}
