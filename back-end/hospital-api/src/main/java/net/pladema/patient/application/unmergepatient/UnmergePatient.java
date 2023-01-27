package net.pladema.patient.application.unmergepatient;

import java.util.ArrayList;
import java.util.List;

import net.pladema.patient.controller.dto.PatientToMergeDto;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.application.port.MergePatientStorage;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnmergePatient {

	private final MergePatientStorage mergePatientStorage;

	private final MergeClinicHistoryStorage mergeClinicHistoryStorage;
	
	public List<Integer> run(Integer institutionId, PatientToMergeDto patientToUnmerge) {
		log.debug("Input parameters -> institutionId {}, patientToUnmerge {}", institutionId, patientToUnmerge);
		mergePatientStorage.assertBasicPersonData(patientToUnmerge.getRegistrationDataPerson());
		Integer patientId = patientToUnmerge.getActivePatientId();
		List<Integer> inactivePatientIds = mergePatientStorage.getInactivePatientsByActivePatientId(patientId);
		inactivePatientIds.forEach(inactivePatientId -> {
			mergePatientStorage.reactivatePatient(inactivePatientId, institutionId);
			mergeClinicHistoryStorage.unmergeClinicData(inactivePatientId);
		});
		mergePatientStorage.deleteMergeHistoricData(patientId, inactivePatientIds);
		mergePatientStorage.updatePersonByPatientId(patientId, patientToUnmerge.getRegistrationDataPerson(), institutionId);
		List<Integer> result = new ArrayList<>(inactivePatientIds);
		result.add(patientId);
		log.debug("Output result {}", result);
		return result;
	}


}
