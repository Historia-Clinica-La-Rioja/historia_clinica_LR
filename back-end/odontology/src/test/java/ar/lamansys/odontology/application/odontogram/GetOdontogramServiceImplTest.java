package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.*;
import ar.lamansys.odontology.infrastructure.repository.tooth.OdontogramQuadrantStorageMockImpl;
import ar.lamansys.odontology.infrastructure.repository.tooth.ToothStorageMockImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GetOdontogramServiceImplTest {

    private GetOdontogramService getOdontogramService;

    @BeforeEach
    public void setUp() {
        ToothStorage toothStorage = new ToothStorageMockImpl();
        OdontogramQuadrantStorage odontogramQuadrantStorage = new OdontogramQuadrantStorageMockImpl();
        getOdontogramService = new GetOdontogramServiceImpl(
                toothStorage,
                odontogramQuadrantStorage
        );
    }

    @Test
    public void odontogramTeethAmountAndPermanentSuccess() {
        List<OdontogramQuadrantBo> resultService = getOdontogramService.run();

        List.of(1,2,3,4).forEach(quadrantCode -> {
            var quadrant = resultService.stream()
                    .filter(q -> q.getCode().equals(quadrantCode)).findFirst().get();

            assertThat(quadrant.getTeeth().size())
                    .isEqualTo(8);
            assertThat(quadrant.isPermanent()).isEqualTo(true);
        });

        List.of(5,6,7,8).forEach(quadrantCode -> {
            var quadrant = resultService.stream()
                    .filter(q -> q.getCode().equals(quadrantCode)).findFirst().get();

            assertThat(quadrant.getTeeth().size())
                    .isEqualTo(5);
            assertThat(quadrant.isPermanent()).isEqualTo(false);
        });
    }

    @Test
    public void odontogramPositionSuccess() {
        List<OdontogramQuadrantBo> resultService = getOdontogramService.run();

        List.of(1,5).forEach(quadrantCode -> {
            var quadrant = resultService.stream()
                    .filter(q -> q.getCode().equals(quadrantCode)).findFirst().get();

            assertThat(quadrant.isLeft())
                    .isEqualTo(false);

            assertThat(quadrant.isTop())
                    .isEqualTo(true);
        });
        List.of(2,6).forEach(quadrantCode -> {
            var quadrant = resultService.stream()
                    .filter(q -> q.getCode().equals(quadrantCode)).findFirst().get();

            assertThat(quadrant.isLeft())
                    .isEqualTo(true);

            assertThat(quadrant.isTop())
                    .isEqualTo(true);
        });
        List.of(3,7).forEach(quadrantCode -> {
            var quadrant = resultService.stream()
                    .filter(q -> q.getCode().equals(quadrantCode)).findFirst().get();

            assertThat(quadrant.isLeft())
                    .isEqualTo(true);

            assertThat(quadrant.isTop())
                    .isEqualTo(false);
        });
        List.of(4,8).forEach(quadrantCode -> {
            var quadrant = resultService.stream()
                    .filter(q -> q.getCode().equals(quadrantCode)).findFirst().get();

            assertThat(quadrant.isLeft())
                    .isEqualTo(false);

            assertThat(quadrant.isTop())
                    .isEqualTo(false);
        });


    }
}

