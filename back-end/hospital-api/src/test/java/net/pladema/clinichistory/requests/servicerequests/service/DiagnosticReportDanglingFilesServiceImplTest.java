package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.UnitRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportFileRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportFile;
import net.pladema.clinichistory.requests.servicerequests.service.impl.DiagnosticReportDanglingFilesServiceServiceImplTest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class DiagnosticReportDanglingFilesServiceImplTest extends UnitRepository {

    private DiagnosticReportDanglingFilesService diagnosticReportDanglingFilesService;

    @Autowired
    private DiagnosticReportFileRepository diagnosticReportFileRepository;

    @Before
    public void setUp() {
        diagnosticReportDanglingFilesService = new DiagnosticReportDanglingFilesServiceServiceImplTest(diagnosticReportFileRepository);
    }

    @Test
    public void test_execute_getDanglingFiles_withInvalidState() {
        var files = populateDiagnosticReportFileRepository();
        var danglingFiles = diagnosticReportDanglingFilesService.run();
        Assertions.assertThat(danglingFiles).hasSize(1);
        Assertions.assertThat(danglingFiles.get(0))
                .isEqualTo(files.get(2).getPath());
    }

    public List<DiagnosticReportFile> populateDiagnosticReportFileRepository() {
        DiagnosticReportFile drf1 = new DiagnosticReportFile("path", "text/plain", 124142L, "file1.txt");
        DiagnosticReportFile drf2 = new DiagnosticReportFile("path", "text/pdf", 4125, "file2.pdf");
        DiagnosticReportFile drf3 = new DiagnosticReportFile("path", "image/jpg", 999L, "file3.jpg");

        drf1.setDiagnosticReportId(1);
        drf2.setDiagnosticReportId(1);

        save(drf1);
        save(drf2);
        save(drf3);

        return List.of(drf1, drf2, drf3);
    }
}
