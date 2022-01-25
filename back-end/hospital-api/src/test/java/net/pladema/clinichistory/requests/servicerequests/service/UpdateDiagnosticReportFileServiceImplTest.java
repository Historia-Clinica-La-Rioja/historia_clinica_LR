package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportFileRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportFile;
import net.pladema.clinichistory.requests.servicerequests.service.impl.UpdateDiagnosticReportFileServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class UpdateDiagnosticReportFileServiceImplTest extends UnitRepository {

    private UpdateDiagnosticReportFileService updateDiagnosticReportFileService;

    @Autowired
    private  DiagnosticReportFileRepository diagnosticReportFileRepository;

    @BeforeEach
    void setUp() {
        updateDiagnosticReportFileService = new UpdateDiagnosticReportFileServiceImpl(
                diagnosticReportFileRepository
        );
    }

    @Test
    void test_execute_updateDiagnosticReportFile() {
        Integer patientId = 5;
        Integer ibuprofenoId = 13;
        var fileIds = populateDiagnosticReportFileRepository();

        Integer diagnosticReportId = save(new DiagnosticReport(patientId, ibuprofenoId, "", null, 9, DiagnosticReportStatus.REGISTERED)).getId();

        updateDiagnosticReportFileService.run(diagnosticReportId, fileIds);

        fileIds.stream().forEach(fileId -> {
            Assertions.assertSame(diagnosticReportFileRepository.findById(fileId).get().getDiagnosticReportId(), diagnosticReportId);
        });
    }

    private List<Integer> populateDiagnosticReportFileRepository() {
        DiagnosticReportFile drf1 = new DiagnosticReportFile("path", "text/plain", 124142L, "file1.txt");
        DiagnosticReportFile drf2 = new DiagnosticReportFile("path", "text/pdf", 4125, "file2.pdf");
        DiagnosticReportFile drf3 = new DiagnosticReportFile("path", "image/jpg", 999L, "file3.jpg");
        Integer id1 = save(drf1).getId();
        Integer id2 = save(drf2).getId();
        Integer id3 = save(drf3).getId();
        return List.of(id1, id2, id3);
    }
}
