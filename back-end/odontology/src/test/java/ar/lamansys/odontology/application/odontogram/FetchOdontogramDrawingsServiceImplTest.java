package ar.lamansys.odontology.application.odontogram;

import ar.lamansys.odontology.domain.consultation.OdontogramDrawingStorage;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.DrawingBo;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchOdontogramDrawingsServiceImplTest {

    private FetchOdontogramDrawingsService fetchOdontogramDrawingsService;

    @Mock
    private OdontogramDrawingStorage odontogramDrawingStorage;

    @BeforeEach
    void setUp() {
        fetchOdontogramDrawingsService = new FetchOdontogramDrawingsServiceImpl(odontogramDrawingStorage);
    }

    @Test
    void shouldFilterTeethWithNoDrawings () {
        Integer patientId = 100;
        List<ToothDrawingsBo> toothDrawingsBos = new ArrayList<>();

        ToothDrawingsBo toothDrawingsBo1 = new ToothDrawingsBo("tooth 1");
        toothDrawingsBo1.setCentralSurfaceDrawing(new DrawingBo("drawing 1"));
        toothDrawingsBo1.setInternalSurfaceDrawing(new DrawingBo("drawing 2"));
        toothDrawingsBos.add(toothDrawingsBo1);

        ToothDrawingsBo toothDrawingsBo2 = new ToothDrawingsBo("tooth 2");
        toothDrawingsBo2.setLeftSurfaceDrawing(new DrawingBo("drawing 1"));
        toothDrawingsBo2.setInternalSurfaceDrawing(new DrawingBo("drawing 2"));
        toothDrawingsBos.add(toothDrawingsBo2);

        ToothDrawingsBo toothDrawingsBo3 = new ToothDrawingsBo("tooth 3");
        toothDrawingsBos.add(toothDrawingsBo3);

        ToothDrawingsBo toothDrawingsBo4 = new ToothDrawingsBo("tooth 4");
        toothDrawingsBo4.setWholeDrawing(new DrawingBo("drawing 3"));
        toothDrawingsBos.add(toothDrawingsBo4);

        when(odontogramDrawingStorage.getDrawings(patientId))
                .thenReturn(toothDrawingsBos);

        List<ToothDrawingsBo> result = fetchOdontogramDrawingsService.run(patientId);

        Assertions.assertEquals(3, result.size());
        Assertions.assertTrue(result.stream().allMatch(ToothDrawingsBo::hasAnyDrawing));
        Assertions.assertTrue(result.stream().noneMatch(t -> t.getToothId().equals("tooth 3")));
    }

}