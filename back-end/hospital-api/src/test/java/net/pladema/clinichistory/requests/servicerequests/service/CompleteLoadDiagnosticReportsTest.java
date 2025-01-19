package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDiagnosticReports;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentFileRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferencePort;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ReferenceRequestClosureBo;
import net.pladema.clinichistory.requests.servicerequests.service.impl.CompleteDiagnosticReportServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

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

	@MockBean
	private DocumentFileRepository documentFileRepository;

	@MockBean
	private SharedReferencePort sharedReferencePort;

    @BeforeEach
    public void setUp() {
        completeDiagnosticReportService = new CompleteDiagnosticReportServiceImpl(
                diagnosticReportRepository,
                documentService,
                loadDiagnosticReports,
                snomedService,
				sharedReferencePort
        );
    }

    @Test
    void test_execute_completeDiagnosticReport_withInvalidState() {
        Integer patientId = 5;
        Integer ibuprofenoId = 13;
		Integer institutionId = 1;

		ReferenceRequestClosureBo requestClosure = new ReferenceRequestClosureBo();
        Integer diagnosticReportId = save(new DiagnosticReport(patientId, ibuprofenoId, "", null, 9, DiagnosticReportStatus.CANCELLED)).getId();

        CompleteDiagnosticReportBo completeDiagnosticReportBo = new CompleteDiagnosticReportBo(
			"Sospecho Inmunodeficiencia cronica",
			"linkaunlink.link",
			requestClosure
        );

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                completeDiagnosticReportService.run(
						patientId,
                        diagnosticReportId,
                        completeDiagnosticReportBo,
						institutionId));
        String expectedMessage = "El estudio con id "+ diagnosticReportId + " no se puede completar porque ha sido cancelado";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));


        Integer diagnosticReportId2 = save(new DiagnosticReport(patientId, ibuprofenoId, "", null, 9, DiagnosticReportStatus.FINAL)).getId();

        exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
                completeDiagnosticReportService.run(
						patientId,
                        diagnosticReportId2,
                        completeDiagnosticReportBo,
						institutionId)
        );
        expectedMessage = "El estudio con id " + diagnosticReportId2 + " no se puede completar porque ya ha sido completado";
        actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}
