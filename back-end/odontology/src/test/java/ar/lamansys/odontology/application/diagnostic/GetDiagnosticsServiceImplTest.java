package ar.lamansys.odontology.application.diagnostic;

import ar.lamansys.odontology.domain.DiagnosticBo;
import ar.lamansys.odontology.domain.DiagnosticStorage;
import ar.lamansys.odontology.infrastructure.repository.DiagnosticStorageMockImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GetDiagnosticsServiceImplTest {

    private GetDiagnosticsService getDiagnosticsService;

    @BeforeEach
    public void setUp() {
        DiagnosticStorage diagnosticStorage = new DiagnosticStorageMockImpl();
        getDiagnosticsService = new GetDiagnosticsServiceImpl(diagnosticStorage);
    }

    @Test
    public void shouldGetDiagnosticsApplicableToTeethAndSurfaces() {
        List<DiagnosticBo> diagnostics = getDiagnosticsService.run();

        // some should be applicable to tooth surfaces
        assertThat(diagnostics.stream().anyMatch(DiagnosticBo::isApplicableToSurface))
                .isEqualTo(true);
        // and some should be applicable to teeth
        assertThat(diagnostics.stream().anyMatch(DiagnosticBo::isApplicableToTooth))
                .isEqualTo(true);

    }

}