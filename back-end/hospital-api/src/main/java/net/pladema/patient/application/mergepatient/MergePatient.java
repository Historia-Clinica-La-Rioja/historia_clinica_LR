package net.pladema.patient.application.mergepatient;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.patient.application.port.exceptions.MergePatientException;
import net.pladema.patient.application.port.exceptions.MergePatientExceptionEnum;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.patient.application.port.MergePatientStorage;
import net.pladema.patient.controller.dto.PatientToMergeDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MergePatient {

	private final MergePatientStorage mergePatientStorage;

	private final MigrateClinicHistory migrateClinicHistory;

	private final InternmentEpisodeService internmentEpisodeService;

	private final EmergencyCareEpisodeService emergencyCareEpisodeService;

	@Transactional
	public Integer run(Integer institutionId, PatientToMergeDto patientToMerge) {
		log.debug("Input parameters -> institutionId {}, patientToMerge {}", institutionId, patientToMerge);
		mergePatientStorage.assertBasicPersonData(patientToMerge.getRegistrationDataPerson());
		Integer activePatientId = patientToMerge.getActivePatientId();

		List<Integer> patients = Stream.concat(patientToMerge.getOldPatientsIds().stream(), Stream.of(activePatientId)).collect(Collectors.toList());
		validatePatientsInternmentEpisodes(patients);
		validatePatientsEmergencyCareEpisodes(patients);

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

	private void validatePatientsInternmentEpisodes(List<Integer> patients) {
		if (internmentEpisodeService.haveMoreThanOneIntermentEpisodesFromPatients(patients)) {
			throw new MergePatientException(MergePatientExceptionEnum.MULTIPLE_INTERNMENT_EPISODES, "Existen episodios de internación activos en más de un paciente.");
		}
	}

	private void validatePatientsEmergencyCareEpisodes(List<Integer> patients) {
		if (emergencyCareEpisodeService.haveMoreThanOneEmergencyCareEpisodeFromPatients(patients)) {
			throw new MergePatientException(MergePatientExceptionEnum.MULTIPLE_EMERGENCY_CARE_EPISODES, "Existen episodios de guardia activos en más de un paciente.");
		}
	}

}
