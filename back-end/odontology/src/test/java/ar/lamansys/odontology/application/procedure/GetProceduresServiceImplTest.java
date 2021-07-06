package ar.lamansys.odontology.application.procedure;

import ar.lamansys.odontology.domain.ProcedureBo;
import ar.lamansys.odontology.domain.ProceduresStorage;
import ar.lamansys.odontology.infrastructure.repository.ProceduresStorageMockImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GetProceduresServiceImplTest {

    GetProceduresService getProceduresService;

    @BeforeEach
    void setUp() {
        ProceduresStorage proceduresStorage = new ProceduresStorageMockImpl();
        getProceduresService = new GetProceduresServiceImpl(proceduresStorage);
    }

    @Test
    void shouldGetProceduresApplicableToTeethAndSurfaces() {
        List<ProcedureBo> procedures = getProceduresService.run();

        // some should be applicable to tooth surfaces
        assertThat(procedures.stream().anyMatch(ProcedureBo::isApplicableToSurface))
                .isEqualTo(true);
        // and some should be applicable to teeth
        assertThat(procedures.stream().anyMatch(ProcedureBo::isApplicableToTooth))
                .isEqualTo(true);

    }
}