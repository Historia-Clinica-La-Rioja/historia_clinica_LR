package net.pladema.patient.mergepatient;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.patient.application.mergepatient.MergePatient;

import net.pladema.patient.application.mergepatient.MigrateClinicHistory;
import net.pladema.patient.application.port.MergeClinicHistoryStorage;
import net.pladema.patient.application.port.MergePatientStorage;

import net.pladema.patient.application.port.MigratePatientStorage;
import net.pladema.patient.application.port.exceptions.MergePatientException;
import net.pladema.patient.application.port.exceptions.MergePatientExceptionEnum;
import net.pladema.patient.application.unmergepatient.UnmergePatient;
import net.pladema.patient.controller.dto.PatientToMergeDto;
import net.pladema.patient.infrastructure.output.repository.MergePatientStorageImpl;
import net.pladema.patient.repository.AdditionalDoctorRepository;
import net.pladema.patient.repository.MergedInactivePatientRepository;
import net.pladema.patient.repository.MergedPatientRepository;
import net.pladema.patient.repository.PatientMedicalCoverageRepository;
import net.pladema.patient.repository.PatientRepository;
import net.pladema.patient.service.PatientService;
import net.pladema.person.controller.dto.BasicPersonalDataDto;
import net.pladema.person.service.PersonService;
import net.pladema.user.repository.UserPersonRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MergePatientStorageExceptionsTest {

	private UnmergePatient unmergePatient;
	private MergePatient mergePatient;

	private MergePatientStorage mergePatientStorage;

	@Mock
	private PatientRepository patientRepository;
	@Mock
	private PatientService patientService;
	@Mock
	private PersonService personService;
	@Mock
	private MergedPatientRepository mergedPatientRepository;
	@Mock
	private MergedInactivePatientRepository mergedInactivePatientRepository;
	@Mock
	private UserPersonRepository userPersonRepository;
	@Mock
	private UserExternalService userExternalService;
	@Mock
	private MigratePatientStorage migratePatientStorage;
	@Mock
	private AdditionalDoctorRepository additionalDoctorRepository;
	@Mock
	private PatientMedicalCoverageRepository patientMedicalCoverageRepository;
	@Mock
	private MigrateClinicHistory migrateClinicHistory;
	@Mock
	private InternmentEpisodeService internmentEpisodeService;
	@Mock
	private EmergencyCareEpisodeService emergencyCareEpisodeService;
	@Mock
	private MergeClinicHistoryStorage mergeClinicHistoryStorage;

	@Mock
	private FeatureFlagsService featureFlagsService;

	@BeforeEach
	void setUp() {
		mergePatientStorage = new MergePatientStorageImpl(patientRepository,patientService,personService,mergedPatientRepository,mergedInactivePatientRepository,
				userPersonRepository,userExternalService,migratePatientStorage,additionalDoctorRepository,patientMedicalCoverageRepository,featureFlagsService);

		mergePatient = new MergePatient(mergePatientStorage,migrateClinicHistory,internmentEpisodeService,emergencyCareEpisodeService);

		unmergePatient = new UnmergePatient(mergePatientStorage, mergeClinicHistoryStorage);
	}

	@Test
	void mergePatient_run_ThrowMergePatientExceptionWithCodeNULL_FIRST_NAME() {
		Integer institutionId = 1;
		Short s = 1;
		PatientToMergeDto patientToMerge = getPatientToMergeDto(Arrays.asList(1,2),3,null,"Doe","11111111",s,"011","22222222",s,"Juan");

		var thrown = catchThrowable(() -> mergePatient.run(institutionId,patientToMerge));

		assertThat(thrown)
				.isInstanceOf(MergePatientException.class)
				.extracting("code")
				.isEqualTo(MergePatientExceptionEnum.NULL_FIRST_NAME);
	}

	@Test
	void mergePatient_run_ThrowMergePatientExceptionWithCodeNULL_IDENTIFICATION_NUMBER() {
		Integer institutionId = 1;
		Short s = 1;
		PatientToMergeDto patientToMerge = getPatientToMergeDto(Arrays.asList(1,2),3,"John","Doe",null,s,"011","22222222",s,"Juan");

		var thrown = catchThrowable(() -> mergePatient.run(institutionId,patientToMerge));

		assertThat(thrown)
				.isInstanceOf(MergePatientException.class)
				.extracting("code")
				.isEqualTo(MergePatientExceptionEnum.NULL_IDENTIFICATION_NUMBER);
	}

	@Test
	void mergePatient_run_ThrowMergePatientExceptionWithCodePATIENT_NOT_EXISTS() {
		Integer institutionId = 1;
		Short s = 1;
		PatientToMergeDto patientToMerge = getPatientToMergeDto(Arrays.asList(1, 2), 3,"John", "Doe", "11111111", s, "011", "22222222", s, "Juan");

		List<Integer> patients = Stream.concat(patientToMerge.getOldPatientsIds().stream(), Stream.of(patientToMerge.getActivePatientId())).collect(Collectors.toList());

		when(internmentEpisodeService.haveMoreThanOneIntermentEpisodesFromPatients(patients))
				.thenReturn(false);
		when(emergencyCareEpisodeService.haveMoreThanOneEmergencyCareEpisodeFromPatients(patients))
				.thenReturn(false);

		when(patientRepository.findById(1))
				.thenReturn(Optional.empty());

		var thrown = catchThrowable(() -> mergePatient.run(institutionId, patientToMerge));

		verify(internmentEpisodeService, times(1)).haveMoreThanOneIntermentEpisodesFromPatients(patients);
		verify(emergencyCareEpisodeService, times(1)).haveMoreThanOneEmergencyCareEpisodeFromPatients(patients);
		assertThat(thrown)
				.isInstanceOf(MergePatientException.class)
				.extracting("code")
				.isEqualTo(MergePatientExceptionEnum.PATIENT_NOT_EXISTS);
	}

	@Test
	void unmergePatient_run_ThrowMergePatientExceptionWithCodePATIENT_INACTIVE_NOT_EXISTS() {
		Integer institutionId = 1;
		Short s = 1;
		List<Integer> inactivePatientsIds = Arrays.asList(1,2);

		PatientToMergeDto patientToMerge = getPatientToMergeDto(inactivePatientsIds,3,"John","Doe","11111111",s,"011","22222222",s,"Juan");

		when(mergedInactivePatientRepository.findAllInactivePatientIdByActivePatientId(patientToMerge.getActivePatientId()))
				.thenReturn(inactivePatientsIds);
		when(mergedInactivePatientRepository.findByInactivePatientId(1))
				.thenReturn(Optional.empty());

		var thrown = catchThrowable(() -> unmergePatient.run(institutionId, patientToMerge));

		verify(mergedInactivePatientRepository, times(1)).findAllInactivePatientIdByActivePatientId(patientToMerge.getActivePatientId());
		assertThat(thrown)
				.isInstanceOf(MergePatientException.class)
				.extracting("code")
				.isEqualTo(MergePatientExceptionEnum.PATIENT_INACTIVE_NOT_EXISTS);

	}

	PatientToMergeDto getPatientToMergeDto(List<Integer> inactivePatientsIds, Integer activePatientId,String firstName, String lastName, String identificationNumber, Short identificationTypeId, String phonePrefix, String phoneNumber, Short genderId, String nameSelfDetermination) {
		return new PatientToMergeDto(inactivePatientsIds,activePatientId, new BasicPersonalDataDto(
				firstName,lastName,identificationNumber,identificationTypeId,phonePrefix,phoneNumber,genderId,nameSelfDetermination, LocalDate.of(1996, 1, 8)
		));
	}
}
