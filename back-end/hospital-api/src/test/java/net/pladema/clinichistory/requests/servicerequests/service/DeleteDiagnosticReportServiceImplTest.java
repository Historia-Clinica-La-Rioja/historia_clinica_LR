package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.documents.repository.ips.DiagnosticReportRepository;
import net.pladema.clinichistory.documents.repository.ips.DosageRepository;
import net.pladema.clinichistory.documents.repository.ips.MedicationStatementRepository;
import net.pladema.clinichistory.documents.repository.ips.entity.DiagnosticReport;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DiagnosticReportStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.service.DocumentService;
import net.pladema.clinichistory.documents.service.NoteService;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.CreateMedicationService;
import net.pladema.clinichistory.documents.service.ips.DiagnosticReportService;
import net.pladema.clinichistory.documents.service.ips.SnomedService;
import net.pladema.clinichistory.mocks.DiagnosticReportTestMocks;
import net.pladema.clinichistory.mocks.MedicationTestMocks;
import net.pladema.clinichistory.requests.medicationrequests.service.ChangeStateMedicationService;
import net.pladema.clinichistory.requests.medicationrequests.service.impl.ChangeStateMedicationServiceImpl;
import net.pladema.clinichistory.requests.servicerequests.service.impl.DeleteDiagnosticReportServiceImpl;
import net.pladema.sgx.dates.configuration.DateTimeProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DataJpaTest
public class DeleteDiagnosticReportServiceImplTest extends UnitRepository {


    private DeleteDiagnosticReportService deleteDiagnosticReportService;


    @Autowired
    private DiagnosticReportRepository diagnosticReportRepository;

    @MockBean
    private DocumentService documentService;

    @MockBean
    DiagnosticReportService diagnosticReportService;

    @MockBean
    private SnomedService snomedService;

    @MockBean
    private CreateMedicationService createMedicationService;

    @MockBean
    private NoteService noteService;

    @Before
    public void setUp() {
        deleteDiagnosticReportService = new DeleteDiagnosticReportServiceImpl(
                diagnosticReportRepository,
                noteService,
                documentService,
                diagnosticReportService,
                snomedService
                );
    }

    @Test
    public void test_execute_cancelDiagnosticReport_withInvalidState() {
        Integer patientId = 5;
        Integer ibuprofenoId = 13;

        Integer diagnosticReportId = save(new DiagnosticReport(patientId, ibuprofenoId, "", null, 9, DiagnosticReportStatus.CANCELLED)).getId();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                deleteDiagnosticReportService.execute(new PatientInfoBo(patientId, (short) 1, (short) 24), diagnosticReportId)
        );
        String expectedMessage = "El estudio con id "+ diagnosticReportId + " no se puede cancelar debido a que ya está cancelado";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer diagnosticReportId2 = save(new DiagnosticReport(patientId, ibuprofenoId, "", null, 9, DiagnosticReportStatus.FINAL)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                deleteDiagnosticReportService.execute(new PatientInfoBo(patientId, (short) 1, (short) 24), diagnosticReportId2)
        );
        expectedMessage = "El estudio con id "+ diagnosticReportId2 + " no se puede cancelar debido a que ya ha sido completada";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}