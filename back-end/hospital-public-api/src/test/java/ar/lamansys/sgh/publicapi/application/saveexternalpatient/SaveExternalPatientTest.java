package ar.lamansys.sgh.publicapi.application.saveexternalpatient;

import ar.lamansys.sgh.publicapi.application.port.out.ExternalPatientStorage;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientBo;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientExtendedBo;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatienExtendedBoException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientBoException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveExternalPatientTest {

    private SaveExternalPatient saveExternalPatient;

    @Mock
    private ExternalPatientStorage externalPatientStorage;

    @BeforeEach
    public void setUp() {
        saveExternalPatient = new SaveExternalPatient(externalPatientStorage);
    }

    @Test
    void saveExistedExternalId() throws ExternalPatientBoException, ExternalPatienExtendedBoException {
        when(externalPatientStorage.findByExternalId(any())).thenReturn(
                java.util.Optional.of(new ExternalPatientBo(1, 2, "1", "1", LocalDateTime.of(2020, 9, 9, 1, 5, 6), "INTERNACION")));

        Integer patientId = saveExternalPatient.run(validExternalPatientExtended());

        verify(externalPatientStorage, times(0)).getPatientId(any(), any(), any());
        verify(externalPatientStorage, times(0)).createPatient(any());

        ArgumentCaptor<ExternalPatientExtendedBo> externalPatientExtendedArgumentCaptor = ArgumentCaptor.forClass(ExternalPatientExtendedBo.class);
        verify(externalPatientStorage, times(1)).save(externalPatientExtendedArgumentCaptor.capture());
        Assertions.assertNull(externalPatientExtendedArgumentCaptor.getValue().getId());
        Assertions.assertEquals(2, externalPatientExtendedArgumentCaptor.getValue().getPatientId());
        Assertions.assertEquals("1", externalPatientExtendedArgumentCaptor.getValue().getExternalId());
        Assertions.assertEquals("2", externalPatientExtendedArgumentCaptor.getValue().getExternalEncounterId());
        Assertions.assertEquals(LocalDateTime.of(2020, 9, 10, 1, 5, 6), externalPatientExtendedArgumentCaptor.getValue().getExternalEncounterDate());
        Assertions.assertEquals("CONSULTA_AMBULATORIA", externalPatientExtendedArgumentCaptor.getValue().getEExternalEncounterType().getValue());
        Assertions.assertEquals(LocalDateTime.of(2020, 12, 13, 0, 0, 0), externalPatientExtendedArgumentCaptor.getValue().getBirthDate());
        Assertions.assertEquals("JUAN", externalPatientExtendedArgumentCaptor.getValue().getFirstName());
        Assertions.assertEquals("GARCIA", externalPatientExtendedArgumentCaptor.getValue().getLastName());
        Assertions.assertEquals((short) 1, externalPatientExtendedArgumentCaptor.getValue().getGenderId());
        Assertions.assertEquals("35565855", externalPatientExtendedArgumentCaptor.getValue().getIdentificationNumber());
        Assertions.assertEquals("2", externalPatientExtendedArgumentCaptor.getValue().getExternalEncounterId());
        Assertions.assertEquals("011252545", externalPatientExtendedArgumentCaptor.getValue().getPhoneNumber());
        Assertions.assertEquals("juan@example.com", externalPatientExtendedArgumentCaptor.getValue().getEmail());

        Assertions.assertEquals(2, patientId);

    }

    private ExternalPatientExtendedBo validExternalPatientExtended() throws ExternalPatientBoException, ExternalPatienExtendedBoException {
        return new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null);
    }

    @Test
    void saveExtistedPatient() throws ExternalPatientBoException, ExternalPatienExtendedBoException {
        when(externalPatientStorage.findByExternalId(any())).thenReturn(Optional.empty());
        when(externalPatientStorage.getPatientId(any(),any(),any())).thenReturn(Optional.of(8));
        Integer patientId = saveExternalPatient.run(validExternalPatientExtended());

        ArgumentCaptor<Short> identificationTypeIdCaptor = ArgumentCaptor.forClass(Short.class);
        ArgumentCaptor<String> identificationNumberCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Short> genderIdCaptor = ArgumentCaptor.forClass(Short.class);

        verify(externalPatientStorage,times(1)).getPatientId(identificationTypeIdCaptor.capture(),identificationNumberCaptor.capture(),genderIdCaptor.capture());
        Assertions.assertEquals((short)1,identificationTypeIdCaptor.getValue());
        Assertions.assertEquals("35565855",identificationNumberCaptor.getValue());
        Assertions.assertEquals((short)1,genderIdCaptor.getValue());
        Assertions.assertEquals(8,patientId);

    }

    @Test
    void invalidExternalPatientData(){
        Exception exception = Assertions.assertThrows(ExternalPatientBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, null, "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("El id externo es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatientBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", null, LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("El id de encuentro externo es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatientBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", null, "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("La fecha de encuentro externo es obligatoria", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatientBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), null, LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("El tipo de encuentro externo es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatienExtendedBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", null, "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("La fecha de nacimiento es obligatoria", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatienExtendedBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), null, (short) 1, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("El nombre es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatienExtendedBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", null, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("El id del genero es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatienExtendedBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, null, (short) 1, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("El número de documento es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatienExtendedBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", null, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("El id de tipo de documento es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatienExtendedBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, null, "011252545", "juan@example.com", null)));
        assertEquals("El apellido es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatienExtendedBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", null, "juan@example.com", null)));
        assertEquals("El número telefónico es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(ExternalPatienExtendedBoException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", "011252545", null, null)));
        assertEquals("El email es obligatorio", exception.getMessage());
        exception = Assertions.assertThrows(NotFoundException.class, () ->
                saveExternalPatient.run(new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null)));
        assertEquals("El tipo de encuentro CONSULTA no existe", exception.getMessage());

    }

    private ExternalPatientExtendedBo invalidExternalPatientExtended() throws ExternalPatientBoException, ExternalPatienExtendedBoException {
        return new ExternalPatientExtendedBo(null, null, "1", "2", LocalDateTime.of(2020, 9, 10, 1, 5, 6), "CONSULTA_AMBULATORIA", LocalDateTime.of(2020, 12, 13, 0, 0, 0), "JUAN", (short) 1, "35565855", (short) 1, "GARCIA", "011252545", "juan@example.com", null);
    }
}
