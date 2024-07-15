package ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter;

import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalEncounterStorage;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalEncounterBo;
import ar.lamansys.sgh.publicapi.patient.domain.exceptions.ExternalEncounterBoException;
import ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter.exceptions.DeleteExternalEncounterAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter.exceptions.DeleteExternalEncounterEnumException;
import ar.lamansys.sgh.publicapi.patient.application.deleteexternalencounter.exceptions.DeleteExternalEncounterException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteExternalEncounterTest {

	@Mock
	private ExternalEncounterStorage externalEncounterStorage;

	@Mock
	private PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	@InjectMocks
	private DeleteExternalEncounter deleteExternalEncounter;

	@BeforeEach
	public void setUp() {
		deleteExternalEncounter = new DeleteExternalEncounter(externalEncounterStorage, patientInformationPublicApiPermission);
	}

	@Test
	void deleteSuccess() throws ExternalEncounterBoException {
		String externalEncounterId = "encounter1";
		Integer institutionId = 1;

		ExternalEncounterBo encounter = new ExternalEncounterBo();
		encounter.setInstitutionId(institutionId);

		when(externalEncounterStorage.get(externalEncounterId)).thenReturn(encounter);
		when(patientInformationPublicApiPermission.canAccessDeleteExternalEncounter()).thenReturn(true);

		deleteExternalEncounter.run(externalEncounterId, institutionId);

		verify(externalEncounterStorage).delete(externalEncounterId);
	}

	@Test
	void deleteAccessDenied() {
		String externalEncounterId = "encounter1";
		Integer institutionId = 1;

		when(patientInformationPublicApiPermission.canAccessDeleteExternalEncounter()).thenReturn(false);

		assertThrows(DeleteExternalEncounterAccessDeniedException.class, () -> {
			deleteExternalEncounter.run(externalEncounterId, institutionId);
		});

		verify(externalEncounterStorage, never()).delete(anyString());
	}

	@Test
	void deleteDifferentInstitution() throws ExternalEncounterBoException {
		String externalEncounterId = "encounter1";
		Integer institutionId = 1;

		ExternalEncounterBo encounter = new ExternalEncounterBo();
		encounter.setInstitutionId(2);

		when(externalEncounterStorage.get(externalEncounterId)).thenReturn(encounter);
		when(patientInformationPublicApiPermission.canAccessDeleteExternalEncounter()).thenReturn(true);

		DeleteExternalEncounterException exception = assertThrows(DeleteExternalEncounterException.class, () -> {
			deleteExternalEncounter.run(externalEncounterId, institutionId);
		});
		assertEquals("La institución 1 no pertenece al encuentro externo", exception.getMessage());

		DeleteExternalEncounterException deleteExternalEncounterException = assertThrows(DeleteExternalEncounterException.class, () -> {
			deleteExternalEncounter.run(externalEncounterId, institutionId);
		});

		assertEquals(DeleteExternalEncounterEnumException.DIFFERENT_INSTITUTION_ID, deleteExternalEncounterException.getCode());

		verify(externalEncounterStorage, never()).delete(anyString());
	}
}