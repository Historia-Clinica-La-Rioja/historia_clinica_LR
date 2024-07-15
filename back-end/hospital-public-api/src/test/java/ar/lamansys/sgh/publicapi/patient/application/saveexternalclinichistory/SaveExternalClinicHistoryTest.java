package ar.lamansys.sgh.publicapi.patient.application.saveexternalclinichistory;

import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalClinicalHistoryStorage;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalClinicalHistoryBo;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalclinicalhistory.SaveExternalClinicalHistory;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalclinicalhistory.exception.SaveExternalClinicHistoryAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveExternalClinicHistoryTest {

	@Mock
	private ExternalClinicalHistoryStorage externalClinicalHistoryStorage;

	@Mock
	private PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	@InjectMocks
	private SaveExternalClinicalHistory saveExternalClinicalHistory;

	private ExternalClinicalHistoryBo externalClinicalHistoryBo;

	@BeforeEach
	void setUp() {
		externalClinicalHistoryBo = ExternalClinicalHistoryBo.builder()
				.patientGender((short) 1)
				.patientDocumentType((short) 1)
				.patientDocumentNumber("12345678")
				.notes("Test notes")
				.consultationDate(LocalDate.of(2023, 7, 10))
				.institution("Test Institution")
				.professionalName("Test Professional")
				.professionalSpecialty("Test Specialty")
				.build();
	}

	@Test
	void testRun_Success() {
		when(patientInformationPublicApiPermission.canAccessSaveExternalClinicHistory()).thenReturn(true);
		when(externalClinicalHistoryStorage.create(externalClinicalHistoryBo)).thenReturn(1);

		Integer result = saveExternalClinicalHistory.run(externalClinicalHistoryBo);

		assertEquals(1, result);

		verify(patientInformationPublicApiPermission).canAccessSaveExternalClinicHistory();
		verify(externalClinicalHistoryStorage).create(externalClinicalHistoryBo);
	}

	@Test
	void testRun_AccessDenied() {
		when(patientInformationPublicApiPermission.canAccessSaveExternalClinicHistory()).thenReturn(false);

		assertThrows(SaveExternalClinicHistoryAccessDeniedException.class, () -> {
			saveExternalClinicalHistory.run(externalClinicalHistoryBo);
		});

		verify(patientInformationPublicApiPermission).canAccessSaveExternalClinicHistory();
	}
}