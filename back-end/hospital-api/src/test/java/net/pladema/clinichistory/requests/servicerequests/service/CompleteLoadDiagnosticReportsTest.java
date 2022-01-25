package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDiagnosticReports;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.impl.CompleteDiagnosticReportServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

class CompleteLoadDiagnosticReportsTest extends UnitRepository {

    private CompleteDiagnosticReportService completeDiagnosticReportService;

    @Autowired
    private DiagnosticReportRepository diagnosticReportRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private LoadDiagnosticReports loadDiagnosticReports;

    @Mock
    private SnomedService snomedService;

    @BeforeEach
    public void setUp() {
        completeDiagnosticReportService = new CompleteDiagnosticReportServiceImpl(
                diagnosticReportRepository,
                documentService,
                loadDiagnosticReports,
                snomedService
        );
    }

    @Test
    void test_execute_completeDiagnosticReport_withInvalidState() {
        Integer patientId = 5;
        Integer ibuprofenoId = 13;

        Integer diagnosticReportId = save(new DiagnosticReport(patientId, ibuprofenoId, "", null, 9, DiagnosticReportStatus.CANCELLED)).getId();

        CompleteDiagnosticReportBo completeDiagnosticReportBo = new CompleteDiagnosticReportBo(
                "Sospecho Inmunodeficiencia cronica",
                "linkaunlink.link"
        );

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                completeDiagnosticReportService.run(
                        new PatientInfoBo(patientId, (short) 1, (short) 24),
                        diagnosticReportId,
                        completeDiagnosticReportBo));
        String expectedMessage = "El estudio con id "+ diagnosticReportId + " no se puede completar porque ha sido cancelado";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer diagnosticReportId2 = save(new DiagnosticReport(patientId, ibuprofenoId, "", null, 9, DiagnosticReportStatus.FINAL)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                completeDiagnosticReportService.run(
                        new PatientInfoBo(patientId, (short) 1, (short) 24),
                        diagnosticReportId2,
                        completeDiagnosticReportBo)
        );
        expectedMessage = "El estudio con id " + diagnosticReportId2 + " no se puede completar porque ya ha sido completado";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
