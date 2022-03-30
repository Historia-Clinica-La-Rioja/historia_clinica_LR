package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadMedications;
import ar.lamansys.sgh.clinichistory.domain.ips.services.MedicationCalculateStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DosageRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.MedicationStatementRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.mocks.MedicationTestMocks;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.medicationrequests.service.ChangeStateMedicationService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.ChangeStateMedicationRequestBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

class ChangeStateMedicationServiceImplTest extends UnitRepository {

    private ChangeStateMedicationService changeStateMedicationService;

    @Autowired
    private DosageRepository dosageRepository;

    @Autowired
    private MedicationStatementRepository medicationStatementRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private SnomedService snomedService;

    @Mock
    private NoteService noteService;

    @Mock
    private LoadMedications loadMedications;

    @Mock
    private DateTimeProvider dateTimeProvider;

    @BeforeEach
    void setUp() {
        MedicationCalculateStatus medicationCalculateStatus = new MedicationCalculateStatus(dateTimeProvider);
        changeStateMedicationService = new ChangeStateMedicationServiceImpl(medicationStatementRepository,
                loadMedications, dosageRepository, documentService, snomedService, medicationCalculateStatus, noteService, dateTimeProvider);
    }

    @Test
    void test_execute_withInvalidPatientInfo(){
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(null, new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, 2d, null, Collections.emptyList()))
        );
        String expectedMessage = "La información del paciente es obligatoria";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(null, (short)1, (short)4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, 2d, null, Collections.emptyList()))
        );
        expectedMessage = "El código identificador del paciente es obligatorio";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void test_execute_withInvalidNewStatus() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(1, (short)1, (short)4), new ChangeStateMedicationRequestBo(null, 2d, null, Collections.emptyList()))
        );
        String expectedMessage = "El estado es obligatorio";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(1, (short)1, (short)4),  new ChangeStateMedicationRequestBo("ESTADO", 2d, null, Collections.emptyList()))
        );
        expectedMessage = "El estado de la medicación es invalido";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void test_execute_suspendedMedication_withInvalidStateMedication() {

        Integer patientId = 5;
        Integer ibuprofenoId = 13;


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 24));
        Integer dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), null, null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, dosageId)).getId();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short)1, (short)4),  new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, 2d, null, List.of(medicationId)))
        );
        String expectedMessage = "La medicación con id "+ medicationId + " no se puede suspender porque ya esta finalizada";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer medication2Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short)1, (short)4),  new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, 2d, null, List.of(medication2Id)))
        );
        expectedMessage = "La medicación con id "+ medication2Id + " no se puede suspender porque ya esta finalizada";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Integer medication3Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.SUSPENDED, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short)1, (short)4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, 2d, null, List.of(medication3Id)))
        );
        expectedMessage = "La medicación con id "+ medication3Id + " no se puede suspender porque ya esta suspendida";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 21));
        dosageId = save(MedicationTestMocks.createDosage(7d,"d", 8, "h", false,
				LocalDateTime.of(2020,12,16,0,0,0), LocalDateTime.of(2020,12,23,0,0,0), LocalDate.of(2020,12,18), LocalDate.of(2020,12,21), null)).getId();
        Integer medication4Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.SUSPENDED, null, 9, dosageId)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short)1, (short)4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, 2d, null, List.of(medication4Id)))
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
    void test_execute_activeMedication_withInvalidStateMedication() {
        Integer patientId = 5;
        Integer ibuprofenoId = 13;


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 24));
        Integer dosageId = save(MedicationTestMocks.createDosage(7d, "d", 8, "h", false,
				LocalDateTime.of(2020, 12, 16,0,0,0), LocalDateTime.of(2020, 12, 23,0,0,0), null, null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, dosageId)).getId();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, 2d, null, List.of(medicationId)))
        );
        String expectedMessage = "La medicación con id " + medicationId + " no se puede activar porque ya esta finalizada";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer medication2Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, 2d, null, List.of(medication2Id)))
        );
        expectedMessage = "La medicación con id " + medication2Id + " no se puede activar porque ya esta finalizada";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Integer medication3Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.ACTIVE, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, 2d, null, List.of(medication3Id)))
        );
        expectedMessage = "La medicación con id " + medication3Id + " no se puede activar porque ya esta activa";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 20));
        dosageId = save(MedicationTestMocks.createDosage(7d, "d", 8, "h", false,
				LocalDateTime.of(2020, 12, 16,0,0,0), LocalDateTime.of(2020, 12, 23,0,0,0), LocalDate.of(2020, 12, 18), LocalDate.of(2020, 12, 19), null)).getId();
        Integer medication4Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.SUSPENDED, null, 9, dosageId)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, 2d, null, List.of(medication4Id)))
        );
        expectedMessage = "La medicación con id " + medication4Id + " no se puede activar porque ya esta activa";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void test_execute_finalizeMedication_withInvalidStateMedication() {
        Integer patientId = 5;
        Integer ibuprofenoId = 13;


        when(dateTimeProvider.nowDate()).thenReturn(LocalDate.of(2020, 12, 24));
        Integer dosageId = save(MedicationTestMocks.createDosage(7d, "d", 8, "h", false,
				LocalDateTime.of(2020, 12, 16,0,0,0), LocalDateTime.of(2020, 12, 23,0,0,0), null, null, null)).getId();
        Integer medicationId = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, dosageId)).getId();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4),  new ChangeStateMedicationRequestBo(MedicationStatementStatus.STOPPED, 2d, null, List.of(medicationId)))
        );
        String expectedMessage = "La medicación con id " + medicationId + " no se puede finalizar porque ya esta finalizada";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer medication2Id = save(MedicationTestMocks.createMedicationStatement(patientId, ibuprofenoId, "", MedicationStatementStatus.STOPPED, null, 9, null)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                changeStateMedicationService.execute(new PatientInfoBo(patientId, (short) 1, (short) 4), new ChangeStateMedicationRequestBo(MedicationStatementStatus.STOPPED, 2d, null, List.of(medication2Id)))
        );
        expectedMessage = "La medicación con id " + medication2Id + " no se puede finalizar porque ya esta finalizada";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void test_execute_success(){
        PatientInfoBo patient = new PatientInfoBo(1, (short) 1, (short) 18);
        changeStateMedicationService.execute(patient, new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, 2d, null, Collections.emptyList()));
        Assertions.assertTrue(true);
    }

}