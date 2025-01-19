package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDiagnosticReports;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.servicerequests.service.impl.DeleteDiagnosticReportServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class DeleteLoadDiagnosticReportsTest extends UnitRepository {


    private DeleteDiagnosticReportService deleteDiagnosticReportService;


    @Autowired
    private DiagnosticReportRepository diagnosticReportRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private LoadDiagnosticReports loadDiagnosticReports;

    @Mock
    private SnomedService snomedService;

	@MockBean
	private DocumentFileRepository documentFileRepository;

    @BeforeEach
    void setUp() {
        deleteDiagnosticReportService = new DeleteDiagnosticReportServiceImpl(
                diagnosticReportRepository,
                documentService,
                loadDiagnosticReports,
                snomedService
                );
    }

    @Test
    void test_execute_cancelDiagnosticReport_withInvalidState() {
        Integer patientId = 5;
        Integer ibuprofenoId = 13;

        Integer diagnosticReportId = save(new DiagnosticReport(patientId, ibuprofenoId, "", null, 9, DiagnosticReportStatus.CANCELLED)).getId();

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                deleteDiagnosticReportService.execute(patientId, diagnosticReportId)
        );
        String expectedMessage = "El estudio con id "+ diagnosticReportId + " no se puede cancelar debido a que ya está cancelado";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer diagnosticReportId2 = save(new DiagnosticReport(patientId, ibuprofenoId, "", null, 9, DiagnosticReportStatus.FINAL)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                deleteDiagnosticReportService.execute(patientId, diagnosticReportId2)
        );
        expectedMessage = "El estudio con id "+ diagnosticReportId2 + " no se puede cancelar debido a que ya ha sido completada";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}