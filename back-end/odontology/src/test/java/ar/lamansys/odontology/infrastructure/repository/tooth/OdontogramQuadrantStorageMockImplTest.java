package ar.lamansys.odontology.infrastructure.repository.tooth;

import ar.lamansys.odontology.application.fetchodontogram.GetOdontogramInfoServiceImpl;
import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import ar.lamansys.odontology.domain.OdontogramQuadrantStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OdontogramQuadrantStorageMockImplTest {


    @BeforeEach
    public void setUp() {
    }

    @Test
    @DisplayName("Test that of teeth groups is 8")
    public void odontology_teethGroups_masterdata() {
        OdontogramQuadrantStorage odontogramQuadrantStorage = new OdontogramQuadrantStorageMockImpl();
        List<OdontogramQuadrantBo> resultService = odontogramQuadrantStorage.getAll();
        assertThat(resultService.size()).isEqualTo(8);
    }
}