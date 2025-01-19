package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.patient.domain.ExternalClinicalHistoryBo;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalclinicalhistory.SaveExternalClinicalHistory;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalClinicalHistoryDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExternalClinicHistoryControllerTest {

	@Mock
	private SaveExternalClinicalHistory saveExternalClinicalHistory;

	@InjectMocks
	private ExternalClinicHistoryController externalClinicHistoryController;

	private ExternalClinicalHistoryDto externalClinicalHistoryDto;
	private ExternalClinicalHistoryBo externalClinicalHistoryBo;

	@BeforeEach
	public void setUp() {
		externalClinicalHistoryDto = ExternalClinicalHistoryDto.builder()
				.patientGender((short) 1)
				.patientDocumentType((short) 1)
				.patientDocumentNumber("12345678")
				.notes("notes")
				.consultationDate(LocalDate.of(2023, 7, 10))
				.institution("institution")
				.professionalName("professional")
				.professionalSpecialty("specialty")
				.build();

		externalClinicalHistoryBo = ExternalClinicalHistoryBo.builder()
				.patientGender((short) 1)
				.patientDocumentType((short) 1)
				.patientDocumentNumber("12345678")
				.notes("notes")
				.consultationDate(LocalDate.of(2023, 7, 10))
				.institution("institution")
				.professionalName("professional")
				.professionalSpecialty("specialty")
				.build();
	}

	@Test
	void testSave() {
		when(saveExternalClinicalHistory.run(any(ExternalClinicalHistoryBo.class))).thenReturn(1);

		Integer result = externalClinicHistoryController.save(externalClinicalHistoryDto);

		assertEquals(1, result);

	}
}