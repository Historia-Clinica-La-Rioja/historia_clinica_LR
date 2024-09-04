package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.patient.domain.ExternalPatientExtendedBo;
import ar.lamansys.sgh.publicapi.patient.domain.exceptions.ExternalPatientBoException;
import ar.lamansys.sgh.publicapi.patient.domain.exceptions.ExternalPatientExtendedBoException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.dto.ExternalPatientExtendedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalCoverageDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalpatient.SaveExternalPatient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExternalPatientControllerTest {

	@Mock
	private SaveExternalPatient saveExternalPatient;

	@InjectMocks
	private ExternalPatientController externalPatientController;

	private ExternalPatientExtendedDto externalPatientExtendedDto;

	@BeforeEach
	public void setUp() {
		ExternalCoverageDto coverageDto = new ExternalCoverageDto(1, "20-12345678-9", "plan", "coverage name", (short) 1);
		ExternalPatientCoverageDto patientCoverageDto = new ExternalPatientCoverageDto(coverageDto, "12345", true, LocalDate.now(), (short) 1);

		externalPatientExtendedDto = new ExternalPatientExtendedDto(
				1,
				"externalId123",
				LocalDateTime.of(1990, 1, 1, 0, 0),
				"firstName",
				(short) 1,
				"12345678",
				(short) 1,
				"lastName",
				"1111111",
				"email@example.com",
				Collections.singletonList(patientCoverageDto),
				1
		);
	}

	@Test
	void testSave() throws ExternalPatientBoException, ExternalPatientExtendedBoException {
		when(saveExternalPatient.run(any(ExternalPatientExtendedBo.class))).thenReturn(1);

		Integer result = externalPatientController.save(externalPatientExtendedDto);

		verify(saveExternalPatient).run(any(ExternalPatientExtendedBo.class));
		assertEquals(1, result);
	}
}
