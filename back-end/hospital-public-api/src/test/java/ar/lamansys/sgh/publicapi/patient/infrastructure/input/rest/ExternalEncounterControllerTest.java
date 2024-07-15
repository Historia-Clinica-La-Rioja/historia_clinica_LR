package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.patient.domain.ExternalEncounterBo;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.dto.ExternalEncounterDto;
import ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter.DeleteExternalEncounter;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter.SaveExternalEncounter;
import ar.lamansys.sgh.publicapi.patient.domain.exceptions.ExternalEncounterBoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExternalEncounterControllerTest {

	@Mock
	private SaveExternalEncounter saveExternalEncounter;

	@Mock
	private DeleteExternalEncounter deleteExternalEncounter;

	@InjectMocks
	private ExternalEncounterController externalEncounterController;

	private ExternalEncounterDto externalEncounterDto;

	@BeforeEach
	public void setUp() {
		Integer id = 123;
		String externalEncounterId = "enc12345";
		LocalDateTime externalEncounterDate = LocalDateTime.of(2024, 7, 12, 14, 30);
		externalEncounterDto = new ExternalEncounterDto(id, externalEncounterId, externalEncounterDate, "INTERNACION");
	}

	@Test
	void testSave() throws ExternalEncounterBoException {
		doNothing().when(saveExternalEncounter).run(any(ExternalEncounterBo.class));

		externalEncounterController.save("externalId123", 1, externalEncounterDto);

		verify(saveExternalEncounter).run(any(ExternalEncounterBo.class));
	}

	@Test
	void testDelete() throws ExternalEncounterBoException {
		doNothing().when(deleteExternalEncounter).run(any(String.class), any(Integer.class));

		externalEncounterController.delete("externalId123", 1, "enc12345");

		verify(deleteExternalEncounter).run(any(String.class), any(Integer.class));
	}
}