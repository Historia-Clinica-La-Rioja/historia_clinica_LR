package net.pladema.patient.mergepatient;

import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.patient.application.mergepatient.MergePatient;

import net.pladema.patient.application.mergepatient.MigrateClinicHistory;
import net.pladema.patient.application.port.MergePatientStorage;

import net.pladema.patient.application.port.exceptions.MergePatientException;
import net.pladema.patient.application.port.exceptions.MergePatientExceptionEnum;
import net.pladema.patient.controller.dto.PatientToMergeDto;

import net.pladema.person.controller.dto.BasicPersonalDataDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MergePatientTest {
	private MergePatient mergePatient;
	@Mock
	private MergePatientStorage mergePatientStorage;
	@Mock
	private MigrateClinicHistory migrateClinicHistory;
	@Mock
	private InternmentEpisodeService internmentEpisodeService;
	@Mock
	private EmergencyCareEpisodeService emergencyCareEpisodeService;

	@BeforeEach
	void setUp() {
		mergePatient = new MergePatient(mergePatientStorage,migrateClinicHistory,internmentEpisodeService,emergencyCareEpisodeService);
	}

	@Test
	void mergePatient_run_completed() {
		Integer institutionId = 1;
		Short s = 1;
		List<Integer> oldPatientsIds = Arrays.asList(1,2);
		PatientToMergeDto patientToMerge = getPatientToMergeDto(oldPatientsIds,3,"John","Doe","11111111",s,"011","22222222",s,"Juan");

		List<Integer> patients = Stream.concat(patientToMerge.getOldPatientsIds().stream(), Stream.of(patientToMerge.getActivePatientId())).collect(Collectors.toList());

		when(internmentEpisodeService.haveMoreThanOneIntermentEpisodesFromPatients(patients))
				.thenReturn(false);
		when(emergencyCareEpisodeService.haveMoreThanOneEmergencyCareEpisodeFromPatients(patients))
				.thenReturn(false);

		mergePatient.run(institutionId,patientToMerge);

		verify(mergePatientStorage, times(1)).assertBasicPersonData(patientToMerge.getRegistrationDataPerson());
		verify(internmentEpisodeService, times(1)).haveMoreThanOneIntermentEpisodesFromPatients(patients);
		verify(emergencyCareEpisodeService, times(1)).haveMoreThanOneEmergencyCareEpisodeFromPatients(patients);

		oldPatientsIds.forEach(patientId ->
				verify(mergePatientStorage, times(1)).inactivatePatient(patientId,patientToMerge.getActivePatientId(),institutionId));

		verify(mergePatientStorage, times(1)).updatePersonByPatientId(patientToMerge.getActivePatientId(), patientToMerge.getRegistrationDataPerson(), institutionId);
		verify(mergePatientStorage, times(1)).saveMergeHistoricData(patientToMerge.getActivePatientId(), patientToMerge.getOldPatientsIds());
		verify(migrateClinicHistory, times(1)).execute(patientToMerge.getOldPatientsIds(), patientToMerge.getActivePatientId());
		verify(mergePatientStorage, times(1)).modifyAdditionalDoctor(patientToMerge.getOldPatientsIds(), patientToMerge.getActivePatientId());
		verify(mergePatientStorage, times(1)).modifyPatientMedicalCoverage(patientToMerge.getOldPatientsIds(), patientToMerge.getActivePatientId());

	}

	@Test
	void mergePatient_run_ThrowMergePatientExceptionWithCodeMULTIPLE_INTERNMENT_EPISODES() {
		Integer institutionId = 1;
		Short s = 1;
		PatientToMergeDto patientToMerge = getPatientToMergeDto(Arrays.asList(1,2),3,"John","Doe","11111111",s,"011","22222222",s,"Juan");

		List<Integer> patients = Stream.concat(patientToMerge.getOldPatientsIds().stream(), Stream.of(patientToMerge.getActivePatientId())).collect(Collectors.toList());

		when(internmentEpisodeService.haveMoreThanOneIntermentEpisodesFromPatients(patients)).thenReturn(true);

		var thrown = catchThrowable(() -> mergePatient.run(institutionId,patientToMerge));

		verify(mergePatientStorage, times(1)).assertBasicPersonData(patientToMerge.getRegistrationDataPerson());
		verify(internmentEpisodeService, times(1)).haveMoreThanOneIntermentEpisodesFromPatients(patients);
		assertThat(thrown)
				.isInstanceOf(MergePatientException.class)
				.extracting("code")
				.isEqualTo(MergePatientExceptionEnum.MULTIPLE_INTERNMENT_EPISODES);

	}

	@Test
	void mergePatient_run_ThrowMergePatientExceptionWithCodeMULTIPLE_EMERGENCY_CARE_EPISODES() {
		Integer institutionId = 1;
		Short s = 1;
		PatientToMergeDto patientToMerge = getPatientToMergeDto(Arrays.asList(1, 2), 3, "John", "Doe", "11111111", s, "011", "22222222", s, "Juan");

		List<Integer> patients = Stream.concat(patientToMerge.getOldPatientsIds().stream(), Stream.of(patientToMerge.getActivePatientId())).collect(Collectors.toList());

		when(internmentEpisodeService.haveMoreThanOneIntermentEpisodesFromPatients(patients))
				.thenReturn(false);
		when(emergencyCareEpisodeService.haveMoreThanOneEmergencyCareEpisodeFromPatients(patients))
				.thenReturn(true);

		var thrown = catchThrowable(() -> mergePatient.run(institutionId, patientToMerge));

		verify(mergePatientStorage, times(1)).assertBasicPersonData(patientToMerge.getRegistrationDataPerson());
		verify(internmentEpisodeService, times(1)).haveMoreThanOneIntermentEpisodesFromPatients(patients);
		assertThat(thrown)
				.isInstanceOf(MergePatientException.class)
				.extracting("code")
				.isEqualTo(MergePatientExceptionEnum.MULTIPLE_EMERGENCY_CARE_EPISODES);
	}

	PatientToMergeDto getPatientToMergeDto(List<Integer> inactivePatientsIds, Integer activePatientId,String firstName, String lastName, String identificationNumber, Short identificationTypeId, String phonePrefix, String phoneNumber, Short genderId, String nameSelfDetermination) {
		return new PatientToMergeDto(inactivePatientsIds,activePatientId, new BasicPersonalDataDto(
				firstName,lastName,identificationNumber,identificationTypeId,phonePrefix,phoneNumber,genderId,nameSelfDetermination, LocalDate.of(1996, 1, 8)
		));
	}
}
