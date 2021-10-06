package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.OdontogramQuadrantBo;
import ar.lamansys.odontology.domain.OdontogramQuadrantData;
import ar.lamansys.odontology.domain.ToothStorage;
import ar.lamansys.odontology.infrastructure.repository.ToothStorageMockImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class GetToothWithPositionServiceImplTest {

    private GetToothWithPositionService getToothWithPositionService;

    @BeforeEach
    public void setUp() {
        ToothStorage toothStorage = new ToothStorageMockImpl();
        GetToothService getToothService = new GetToothServiceImpl(toothStorage);
        getToothWithPositionService = new GetToothWithPositionServiceImpl(getToothService);
    }

    @Test
    public void toothPositionCreationSuccess() {
        var toothWithPositionBo = getToothWithPositionService.run("23232311");
        OdontogramQuadrantBo odontogramQuadrantBo = OdontogramQuadrantData.getAsMap().get((short)1);
        Assertions.assertThat(toothWithPositionBo.isLeft()).isEqualTo(odontogramQuadrantBo.isLeft());
        Assertions.assertThat(toothWithPositionBo.isTop()).isEqualTo(odontogramQuadrantBo.isTop());
        Assertions.assertThat(toothWithPositionBo.isPosterior()).isEqualTo(false);
        Assertions.assertThat(toothWithPositionBo.getSnomed().getSctid()).isEqualTo("23232311");
        Assertions.assertThat(toothWithPositionBo.getToothCode()).isEqualTo((short)1);
        Assertions.assertThat(toothWithPositionBo.getQuadrantCode()).isEqualTo(odontogramQuadrantBo.getCode());


        toothWithPositionBo = getToothWithPositionService.run("23232385");
        odontogramQuadrantBo = OdontogramQuadrantData.getAsMap().get((short)8);
        Assertions.assertThat(toothWithPositionBo.isLeft()).isEqualTo(odontogramQuadrantBo.isLeft());
        Assertions.assertThat(toothWithPositionBo.isTop()).isEqualTo(odontogramQuadrantBo.isTop());
        Assertions.assertThat(toothWithPositionBo.isPosterior()).isEqualTo(true);
        Assertions.assertThat(toothWithPositionBo.getSnomed().getSctid()).isEqualTo("23232385");
        Assertions.assertThat(toothWithPositionBo.getToothCode()).isEqualTo((short)5);
        Assertions.assertThat(toothWithPositionBo.getQuadrantCode()).isEqualTo(odontogramQuadrantBo.getCode());
    }

}