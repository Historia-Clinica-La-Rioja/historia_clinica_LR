package ar.lamansys.odontology.application.fetchodontogram;

import ar.lamansys.odontology.domain.*;
import ar.lamansys.odontology.infrastructure.repository.tooth.OdontogramQuadrantStorageMockImpl;
import ar.lamansys.odontology.infrastructure.repository.tooth.ToothStorageMockImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GetOdontogramInfoServiceImplTest {

    ToothStorage toothStorage;
    OdontogramQuadrantStorage odontogramQuadrantStorage;
    GetOdontogramInfoService getOdontogramInfoService;

    @BeforeEach
    public void setUp() {
        toothStorage = new ToothStorageMockImpl();
        odontogramQuadrantStorage = new OdontogramQuadrantStorageMockImpl();
        getOdontogramInfoService = new GetOdontogramInfoServiceImpl(
                toothStorage,
                odontogramQuadrantStorage
        );
    }


    @org.junit.Test
    @DisplayName("Test that teeth amount is 52")
    public void odontology_teeth_masterdata() {
        ToothStorage toothStorage = new ToothStorageMockImpl();
        List<ToothBo> resultService = toothStorage.getAll();
        assertThat(resultService.size()).isEqualTo(52);
    }

    @org.junit.Test
    @DisplayName("Test that of teeth groups is 8")
    public void odontology_teethGroups_masterdata() {
        OdontogramQuadrantStorage odontogramQuadrantStorage = new OdontogramQuadrantStorageMockImpl();
        List<OdontogramQuadrantBo> resultService = odontogramQuadrantStorage.getAll();
        assertThat(resultService.size()).isEqualTo(8);
    }

    @Test
    @DisplayName("Test teeth order success")
    void odontogramTeethOrder() {
        List<TeethGroupBo> resultService = getOdontogramInfoService.run();

        Integer reverseOrderQuadrant = 1;
        Integer naturalOrderQuadrant = 2;

        assertThat(resultService.stream()
                .filter(tg -> tg.getQuadrant().getQuadrantCode().equals(reverseOrderQuadrant))
                .collect(Collectors.toList()).get(0).getTeeth().get(0).getPosition())
                .isEqualTo(8);

        assertThat(resultService.stream()
                .filter(tg -> tg.getQuadrant().getQuadrantCode().equals(naturalOrderQuadrant))
                .collect(Collectors.toList()).get(0).getTeeth().get(0).getPosition())
                .isEqualTo(1);
    }
}

