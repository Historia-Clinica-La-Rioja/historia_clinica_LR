package net.pladema.patient.application.unmergepatient;

import java.util.List;

import net.pladema.patient.application.port.exceptions.MergePatientException;

import net.pladema.patient.application.port.exceptions.MergePatientExceptionEnum;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;
import net.pladema.patient.application.port.MergePatientStorage;
import net.pladema.patient.controller.dto.PatientToMergeDto;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UnmergePatient {

	private final MergePatientStorage mergePatientStorage;

	private final MergeClinicHistoryStorage mergeClinicHistoryStorage;

	@Transactional
	public Boolean run(Integer institutionId, PatientToMergeDto patientToUnmerge) {
		log.debug("Input parameters -> institutionId {}, patientToUnmerge {}", institutionId, patientToUnmerge);
		mergePatientStorage.assertBasicPersonData(patientToUnmerge.getRegistrationDataPerson());
		Integer patientId = patientToUnmerge.getActivePatientId();
		List<Integer> inactivePatientIds = patientToUnmerge.getOldPatientsIds();
		assertInactivePatientIds(inactivePatientIds, patientId);
		inactivePatientIds.forEach(inactivePatientId -> {
			mergePatientStorage.reactivatePatient(inactivePatientId, institutionId);
			mergeClinicHistoryStorage.unmergeClinicData(inactivePatientId);
		});
		mergePatientStorage.deleteMergeHistoricData(patientId, inactivePatientIds);
		mergePatientStorage.updatePersonByPatientId(patientId, patientToUnmerge.getRegistrationDataPerson(), institutionId);

		mergeClinicHistoryStorage.modifyOdontogram(patientId);
		inactivePatientIds.forEach(mergeClinicHistoryStorage::modifyOdontogram);
		Boolean result = Boolean.TRUE;
		log.debug("Output result {}", result);
		return result;
	}

	private void assertInactivePatientIds(List<Integer> inactivePatientIds, Integer activePatientId){
		List<Integer> currentInactivePatients = mergePatientStorage.getInactivePatientsByActivePatientId(activePatientId);
		inactivePatientIds.forEach(toInactivate -> {
			if(!currentInactivePatients.contains(toInactivate))
				throw new MergePatientException(MergePatientExceptionEnum.PATIENT_INACTIVE_NOT_EXISTS, String.format("El paciente %s no esta fusionado con el paciente %s", toInactivate,activePatientId));
		});
	}


}
