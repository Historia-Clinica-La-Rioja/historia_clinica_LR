package ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter;

import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalEncounterStorage;
import ar.lamansys.sgh.publicapi.patient.application.port.out.ExternalPatientStorage;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter.exceptions.SaveExternalEncounterAccessDeniedException;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter.exceptions.SaveExternalEncounterException;
import ar.lamansys.sgh.publicapi.patient.application.saveexternalencounter.exceptions.SaveExternalEncounterEnumException;
import ar.lamansys.sgh.publicapi.patient.domain.EExternalEncounterType;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalEncounterBo;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalPatientBo;
import ar.lamansys.sgh.publicapi.patient.domain.exceptions.ExternalEncounterBoException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.service.PatientInformationPublicApiPermission;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveExternalEncounterTest {

    private SaveExternalEncounter saveExternalEncounter;

    @Mock
    private ExternalEncounterStorage externalEncounterStorage;

    @Mock
    private ExternalPatientStorage externalPatientStorage;

	@Mock
	private PatientInformationPublicApiPermission patientInformationPublicApiPermission;

	@BeforeEach
    public void setUp() {
        saveExternalEncounter = new SaveExternalEncounter(externalPatientStorage, externalEncounterStorage, patientInformationPublicApiPermission);
    }

    @Test
    public void saveSuccessExternalEncounter() throws ExternalEncounterBoException {
		when(patientInformationPublicApiPermission.canAccessSaveExternalEncounter()).thenReturn(true);
        when(externalPatientStorage.findByExternalId(any()))
                .thenReturn(Optional.of(new ExternalPatientBo(23, "2")));
        when(externalEncounterStorage.existsExternalEncounter(any()))
                .thenReturn(false);

        saveExternalEncounter.run(getValidExternalEncounter());

        ArgumentCaptor<ExternalEncounterBo> externalEncounterArgumentCaptor = ArgumentCaptor.forClass(ExternalEncounterBo.class);
        verify(externalEncounterStorage, times(1)).save(externalEncounterArgumentCaptor.capture());
        Assertions.assertEquals("2", externalEncounterArgumentCaptor.getValue().getExternalId());
        Assertions.assertEquals("1", externalEncounterArgumentCaptor.getValue().getExternalEncounterId());
        Assertions.assertEquals(LocalDateTime.of(2021, 12, 13, 0, 0, 0), externalEncounterArgumentCaptor.getValue().getExternalEncounterDate());
        Assertions.assertEquals(EExternalEncounterType.INTERNACION, externalEncounterArgumentCaptor.getValue().getEExternalEncounterType());
        Assertions.assertEquals(1, externalEncounterArgumentCaptor.getValue().getInstitutionId());

    }

    @Test
    public void saveDuplicatedExternalEncounter() {
		when(patientInformationPublicApiPermission.canAccessSaveExternalEncounter()).thenReturn(true);
		when(externalPatientStorage.findByExternalId(any()))
                .thenReturn(java.util.Optional.of(new ExternalPatientBo(23, "2")));
        when(externalEncounterStorage.existsExternalEncounter(any()))
                .thenReturn(true);
		SaveExternalEncounterException saveExternalEncounterException = assertThrows(SaveExternalEncounterException.class, () ->
				saveExternalEncounter.run(getValidExternalEncounter()));
		assertEquals(SaveExternalEncounterEnumException.EXTERNAL_ENCOUNTER_ID_ALREADY_EXISTS, saveExternalEncounterException.getCode());
        Exception exception = assertThrows(SaveExternalEncounterException.class, () ->
                saveExternalEncounter.run(getValidExternalEncounter()));
        assertEquals("El id de encuentro externo 1 ya existe", exception.getMessage());
        verify(externalEncounterStorage, times(0)).save(any());
    }

    @Test
    public void saveUnexistedExternalId() {
		when(patientInformationPublicApiPermission.canAccessSaveExternalEncounter()).thenReturn(true);
		when(externalPatientStorage.findByExternalId(any()))
                .thenReturn(Optional.empty());
        Exception exception = assertThrows(SaveExternalEncounterException.class, () ->
                saveExternalEncounter.run(getValidExternalEncounter()));
		assertEquals("El id externo 2 no existe", exception.getMessage());
		SaveExternalEncounterException saveExternalEncounterException = assertThrows(SaveExternalEncounterException.class, () ->
				saveExternalEncounter.run(getValidExternalEncounter()));
		assertEquals(SaveExternalEncounterEnumException.UNEXISTED_EXTERNAL_ID, saveExternalEncounterException.getCode());
        verify(externalEncounterStorage, times(0)).existsExternalEncounter(any());
        verify(externalEncounterStorage, times(0)).save(any());
    }

	@Test
	void saveExternalEncounterAccessDenied(){
		when(patientInformationPublicApiPermission.canAccessSaveExternalEncounter()).thenReturn(false);
		assertThrows(SaveExternalEncounterAccessDeniedException.class, () -> {
			saveExternalEncounter.run(getValidExternalEncounter());
		});
	}

    @Test
    public void invalidExternalEncounterData() {
        Exception exception = assertThrows(ExternalEncounterBoException.class, () ->
                saveExternalEncounter.run(new ExternalEncounterBo(null, null, "1", LocalDateTime.of(2021, 12, 13, 0, 0, 0), EExternalEncounterType.INTERNACION,1)));
        assertEquals("El id externo es obligatorio", exception.getMessage());
        exception = assertThrows(ExternalEncounterBoException.class, () ->
                saveExternalEncounter.run(new ExternalEncounterBo(null, "2", null, LocalDateTime.of(2021, 12, 13, 0, 0, 0), EExternalEncounterType.INTERNACION,1)));
        assertEquals("El id de encuentro externo es obligatorio", exception.getMessage());
        exception = assertThrows(ExternalEncounterBoException.class, () ->
                saveExternalEncounter.run(new ExternalEncounterBo(null, "2", "1", null, EExternalEncounterType.INTERNACION,1)));
        assertEquals("La fecha del encuentro externo es obligatoria", exception.getMessage());
        exception = assertThrows(ExternalEncounterBoException.class, () ->
                saveExternalEncounter.run(new ExternalEncounterBo(null, "2", "2", LocalDateTime.of(2021, 12, 13, 0, 0, 0), null,1)));
        assertEquals("El tipo de encuentro externo es obligatorio", exception.getMessage());
        exception = assertThrows(NotFoundException.class, () ->
                saveExternalEncounter.run(new ExternalEncounterBo(null, "2", "1", LocalDateTime.of(2021, 12, 13, 0, 0, 0), EExternalEncounterType.map("CONSULTA"),1)));
        assertEquals("El tipo de encuentro CONSULTA no existe", exception.getMessage());
    }

    private ExternalEncounterBo getValidExternalEncounter() throws ExternalEncounterBoException {
        return new ExternalEncounterBo(null, "2", "1", LocalDateTime.of(2021, 12, 13, 0, 0, 0), EExternalEncounterType.INTERNACION,1);
    }
}