package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.core.ips.MedicationCalculateStatus;
import net.pladema.clinichistory.documents.repository.ips.DosageRepository;
import net.pladema.clinichistory.documents.repository.ips.MedicationStatementRepository;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.CreateMedicationService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.mocks.MedicationTestMocks;
import net.pladema.clinichistory.requests.medicationrequests.service.ChangeStateMedicationService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.ChangeStateMedicationRequestBo;
import net.pladema.sgx.dates.configuration.DateTimeProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ChangeStateMedicationServiceImplTest extends UnitRepository {

    private ChangeStateMedicationService changeStateMedicationService;

    @Autowired
    private DosageRepository dosageRepository;

    @Autowired
    private MedicationStatementRepository medicationStatementRepository;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private SnomedService snomedService;

    @MockBean
    private NoteService noteService;

    @MockBean
    private CreateMedicationService createMedicationService;

    @MockBean
    private DateTimeProvider dateTimeProvider;

    @Before
    public void setUp() {
        MedicationCalculateStatus medicationCalculateStatus = new MedicationCalculateStatus(dateTimeProvider);
        changeStateMedicationService = new ChangeStateMedicationServiceImpl(medicationStatementRepository,
                createMedicationService, dosageRepository, documentService, snomedService, medicationCalculateStatus, noteService, dateTimeProvider);
    }

    @Test
    public void test_execute_withInvalidPatientInfo(){
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(null, new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, (short)2, null, Collections.emptyList()))
        );
        String expectedMessage = "La información del paciente es obligatoria";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(null, (short)1, (short)4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, (short)2, null, Collections.emptyList()))
        );
        expectedMessage = "El código identificador del paciente es obligatorio";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_execute_withInvalidNewStatus() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(1, (short)1, (short)4), new ChangeStateMedicationRequestBo(null, (short)2, null, Collections.emptyList()))
        );
        String expectedMessage = "El estado es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(1, (short)1, (short)4),  new ChangeStateMedicationRequestBo("ESTADO", (short)2, null, Collections.emptyList()))
        );
        expectedMessage = "El estado de la medicación es invalido";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_execute_suspendedMedication_withInvalidStateMedication() {

        Integer patientId = 5;
        Integer ibuprofenoId = 13;


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 24));
        Integer dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
                LocalDate.of(2020,12,16), LocalDate.of(2020,12,23), null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, dosageId)).getId();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short)1, (short)4),  new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, (short)2, null, List.of(medicationId)))
        );
        String expectedMessage = "La medicación con id "+ medicationId + " no se puede suspender porque ya esta finalizada";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer medication2Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short)1, (short)4),  new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, (short)2, null, List.of(medication2Id)))
        );
        expectedMessage = "La medicación con id "+ medication2Id + " no se puede suspender porque ya esta finalizada";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Integer medication3Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.SUSPENDED, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short)1, (short)4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, (short)2, null, List.of(medication3Id)))
        );
        expectedMessage = "La medicación con id "+ medication3Id + " no se puede suspender porque ya esta suspendida";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 21));
        dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
                LocalDate.of(2020,12,16), LocalDate.of(2020,12,23), LocalDate.of(2020,12,18), LocalDate.of(2020,12,21))).getId();
        Integer medication4Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.SUSPENDED, null, 9, dosageId)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short)1, (short)4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, (short)2, null, List.of(medication4Id)))
        );
        expectedMessage = "La medicación con id "+ medication4Id + " no se puede suspender porque ya esta suspendida";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short)1, (short)4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, null, null, List.of(medication4Id)))
        );
        expectedMessage = "La cantidad de dias de suspensión es obligatoria";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_execute_activeMedication_withInvalidStateMedication() {
        Integer patientId = 5;
        Integer ibuprofenoId = 13;


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 24));
        Integer dosageId = save(MedicationTestMocks.createDosage(7d, "d", 8, "h", false,
                LocalDate.of(2020, 12, 16), LocalDate.of(2020, 12, 23), null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, dosageId)).getId();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, (short)2, null, List.of(medicationId)))
        );
        String expectedMessage = "La medicación con id " + medicationId + " no se puede activar porque ya esta finalizada";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer medication2Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, (short)2, null, List.of(medication2Id)))
        );
        expectedMessage = "La medicación con id " + medication2Id + " no se puede activar porque ya esta finalizada";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Integer medication3Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.ACTIVE, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, (short)2, null, List.of(medication3Id)))
        );
        expectedMessage = "La medicación con id " + medication3Id + " no se puede activar porque ya esta activa";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 20));
        dosageId = save(MedicationTestMocks.createDosage(7d, "d", 8, "h", false,
                LocalDate.of(2020, 12, 16), LocalDate.of(2020, 12, 23), LocalDate.of(2020, 12, 18), LocalDate.of(2020, 12, 19))).getId();
        Integer medication4Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.SUSPENDED, null, 9, dosageId)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, (short)2, null, List.of(medication4Id)))
        );
        expectedMessage = "La medicación con id " + medication4Id + " no se puede activar porque ya esta activa";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void test_execute_finalizeMedication_withInvalidStateMedication() {
        Integer patientId = 5;
        Integer ibuprofenoId = 13;


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 24));
        Integer dosageId = save(MedicationTestMocks.createDosage(7d, "d", 8, "h", false,
                LocalDate.of(2020, 12, 16), LocalDate.of(2020, 12, 23), null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, dosageId)).getId();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4),  new ChangeStateMedicationRequestBo(MedicationStatementStatus.STOPPED, (short)2, null, List.of(medicationId)))
        );
        String expectedMessage = "La medicación con id " + medicationId + " no se puede finalizar porque ya esta finalizada";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer medication2Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.STOPPED, (short)2, null, List.of(medication2Id)))
        );
        expectedMessage = "La medicación con id " + medication2Id + " no se puede finalizar porque ya esta finalizada";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void test_execute_success(){
        PatientInfoBo patient = new PatientInfoBo(1, (short) 1, (short) 18);
        changeStateMedicationService.execute(patient, new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, (short)2, null, Collections.emptyList()));
        Assertions.assertTrue(true);
    }

}