package net.pladema.patient.application.mergepatient;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.application.mergepatient.exceptions.MergePatientException;
import net.pladema.patient.application.mergepatient.exceptions.MergePatientExceptionEnum;
import net.pladema.patient.application.port.MergePatientStorage;
import net.pladema.patient.controller.dto.PatientToMergeDto;
import net.pladema.person.controller.dto.BasicPersonalDataDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class MergePatient {

	private final MergePatientStorage mergePatientStorage;

	public Integer run(Integer institutionId, PatientToMergeDto patientToMerge) {
		log.debug("Input parameters -> institutionId {}, patientToMerge {}", institutionId, patientToMerge);
		assertBasicPersonData(patientToMerge.getActivePerson());
		Integer activePatientId = patientToMerge.getActivePatientId();
		patientToMerge.getOldPatientsIds().forEach(id -> mergePatientStorage.inactivatePatient(id, activePatientId, institutionId));
		mergePatientStorage.updatePersonByPatientId(activePatientId, patientToMerge.getActivePerson(), institutionId);
		mergePatientStorage.saveMergeHistoricData(activePatientId,patientToMerge.getOldPatientsIds());
		log.debug("Output result -> {}", activePatientId);
		return activePatientId;
	}

	private void assertBasicPersonData(BasicPersonalDataDto basicData) {
		if (basicData.getFirstName() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_FIRST_NAME, "El nombre de la persona es obligatorio");
		if (basicData.getLastName() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_LAST_NAME, "El apellido de la persona es obligatorio");
		if (basicData.getIdentificationTypeId() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_IDENTIFICATION_TYPE, "El tipo de documento de la persona es obligatorio");
		if (basicData.getIdentificationNumber() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_IDENTIFICATION_NUMBER, "El numero de documento de la persona es obligatorio");
		if (basicData.getBirthDate() == null)
			throw new MergePatientException(MergePatientExceptionEnum.NULL_BIRTH_DATE, "La fecha de nacimiento de la persona es obligatoria");

	}

}
