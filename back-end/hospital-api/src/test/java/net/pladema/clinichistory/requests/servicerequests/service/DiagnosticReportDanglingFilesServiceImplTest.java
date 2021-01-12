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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
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
        DiagnosticReportFile drf1 = new DiagnosticReportFile("path1", "text/plain", 124142L);
        drf1.setDiagnosticReportId(1);
        DiagnosticReportFile drf2 = new DiagnosticReportFile("path2", "text/pdf", 4125);
        drf2.setDiagnosticReportId(1);
        DiagnosticReportFile drf3 = new DiagnosticReportFile("path3", "image/jpg", 999L);
        save(drf1);
        save(drf2);
        save(drf3);
        return List.of(drf1, drf2, drf3);
    }
}
