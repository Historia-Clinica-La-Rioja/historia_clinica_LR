package net.pladema.patient.mergepatient;

import net.pladema.patient.application.port.MergeClinicHistoryStorage;
import net.pladema.patient.application.port.MergePatientStorage;
import net.pladema.patient.application.unmergepatient.UnmergePatient;

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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnmergePatientTest {

	private UnmergePatient unmergePatient;

	@Mock
	private MergePatientStorage mergePatientStorage;
	@Mock
	private MergeClinicHistoryStorage mergeClinicHistoryStorage;

	@BeforeEach
	void setUp() {
		unmergePatient = new UnmergePatient(mergePatientStorage,mergeClinicHistoryStorage);
	}

	@Test
	void unmergePatient_run_completed() {
		Integer institutionId = 1;
		Short s = 1;
		List<Integer> inactivePatientsIds = Arrays.asList(1,2);

		PatientToMergeDto patientToMerge = new PatientToMergeDto(inactivePatientsIds,3, new BasicPersonalDataDto(
				"John","Doe","11111111",s,"011","22222222",s,"Juan", LocalDate.of(1996, 1, 8)
		));

		when(mergePatientStorage.getInactivePatientsByActivePatientId(3))
				.thenReturn(inactivePatientsIds);

		unmergePatient.run(institutionId,patientToMerge);

		verify(mergePatientStorage, times(1)).assertBasicPersonData(patientToMerge.getRegistrationDataPerson());
		verify(mergePatientStorage, times(1)).getInactivePatientsByActivePatientId(patientToMerge.getActivePatientId());

		inactivePatientsIds.forEach(patientId -> {
				verify(mergePatientStorage, times(1)).reactivatePatient(patientId, institutionId);
				verify(mergeClinicHistoryStorage, times(1)).unmergeClinicData(patientId);
				});

		verify(mergePatientStorage, times(1)).deleteMergeHistoricData(patientToMerge.getActivePatientId(), inactivePatientsIds);
		verify(mergePatientStorage, times(1)).updatePersonByPatientId(patientToMerge.getActivePatientId(),patientToMerge.getRegistrationDataPerson(),institutionId);
		verify(mergeClinicHistoryStorage, times(1)).modifyOdontogram(patientToMerge.getActivePatientId());
	}

}
