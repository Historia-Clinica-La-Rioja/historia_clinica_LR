package ar.lamansys.odontology.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class ToothWithPositionBoTest {

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void toothSurfaceSuccess() {
        ToothWithPositionBo toothWithPositionBo = new ToothWithPositionBo();
        toothWithPositionBo.setTop(false);
        toothWithPositionBo.setLeft(false);
        toothWithPositionBo.setPosterior(false);

        ToothSurfacesBo toothSurfacesBo = toothWithPositionBo.getSurfaces();

        assertThat(toothSurfacesBo.getCentral()).isEqualTo(EToothSurfaces.INCISAL.getValue());
        assertThat(toothSurfacesBo.getLeft()).isEqualTo(EToothSurfaces.DISTAL.getValue());
        assertThat(toothSurfacesBo.getRight()).isEqualTo(EToothSurfaces.MESIAL.getValue());
        assertThat(toothSurfacesBo.getExternal()).isEqualTo(EToothSurfaces.LINGUAL.getValue());
        assertThat(toothSurfacesBo.getInternal()).isEqualTo(EToothSurfaces.VESTIBULAR.getValue());


        toothWithPositionBo = new ToothWithPositionBo();
        toothWithPositionBo.setTop(true);
        toothWithPositionBo.setLeft(true);
        toothWithPositionBo.setPosterior(true);

        toothSurfacesBo = toothWithPositionBo.getSurfaces();

        assertThat(toothSurfacesBo.getCentral()).isEqualTo(EToothSurfaces.OCLUSAL.getValue());
        assertThat(toothSurfacesBo.getLeft()).isEqualTo(EToothSurfaces.MESIAL.getValue());
        assertThat(toothSurfacesBo.getRight()).isEqualTo(EToothSurfaces.DISTAL.getValue());
        assertThat(toothSurfacesBo.getExternal()).isEqualTo(EToothSurfaces.VESTIBULAR.getValue());
        assertThat(toothSurfacesBo.getInternal()).isEqualTo(EToothSurfaces.PALATINA.getValue());
    }

}