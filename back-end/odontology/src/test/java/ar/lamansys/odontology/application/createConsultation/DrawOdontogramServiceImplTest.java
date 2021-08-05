package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.domain.ESurfacePosition;
import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.OdontogramDrawingStorage;
import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DrawOdontogramServiceImplTest {

    private DrawOdontogramService drawOdontogramService;

    @Mock
    private OdontogramDrawingStorage odontogramDrawingStorage;

    @BeforeEach
    void setUp() {
        drawOdontogramService = new DrawOdontogramServiceImpl(odontogramDrawingStorage);
    }

    @Test
    void shouldHave2ActionsWhenBothAreDiagnostics () {
        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        OdontologySnomedBo action1 = new OdontologySnomedBo("action 1", "action 1");
        OdontologySnomedBo action2 = new OdontologySnomedBo("action 2", "action 2");

        OdontologySnomedBo tooth1 = new OdontologySnomedBo("tooth 1", "tooth 1");

        dentalActions.add(new ConsultationDentalActionBo(action1, tooth1, null, true));
        dentalActions.add(new ConsultationDentalActionBo(action2, tooth1, ESurfacePosition.RIGHT, true));


        List<ToothDrawingsBo> odontogramDrawings = drawOdontogramService.run(1, dentalActions);

        Assertions.assertEquals(1, odontogramDrawings.size());

        ToothDrawingsBo toothDrawings = odontogramDrawings.get(0);
        Assertions.assertEquals("tooth 1", toothDrawings.getToothId());
        Assertions.assertEquals("action 1", toothDrawings.getWholeDrawing().getSnomed().getSctid());
        Assertions.assertNull(toothDrawings.getInternalSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getExternalSurfaceDrawing());
        Assertions.assertEquals("action 2",toothDrawings.getRightSurfaceDrawing().getSnomed().getSctid());
        Assertions.assertNull(toothDrawings.getLeftSurfaceDrawing());

    }

    @Test
    void proceduresShouldOverlapDiagnostics () {
        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        OdontologySnomedBo action1 = new OdontologySnomedBo("action 1", "action 1");
        OdontologySnomedBo action2 = new OdontologySnomedBo("action 2", "action 2");
        OdontologySnomedBo action3 = new OdontologySnomedBo("action 3", "action 3");

        OdontologySnomedBo tooth1 = new OdontologySnomedBo("tooth 1", "tooth 1");

        dentalActions.add(new ConsultationDentalActionBo(action1, tooth1, null, true));
        dentalActions.add(new ConsultationDentalActionBo(action2, tooth1, ESurfacePosition.RIGHT, true));
        dentalActions.add(new ConsultationDentalActionBo(action3, tooth1, null, false));

        List<ToothDrawingsBo> odontogramDrawings = drawOdontogramService.run(1, dentalActions);


        Assertions.assertEquals(1, odontogramDrawings.size());

        ToothDrawingsBo toothDrawings = odontogramDrawings.get(0);
        Assertions.assertEquals("tooth 1", toothDrawings.getToothId());
        Assertions.assertEquals("action 3", toothDrawings.getWholeDrawing().getSnomed().getSctid());
        Assertions.assertNull(toothDrawings.getInternalSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getExternalSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getRightSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getLeftSurfaceDrawing());

    }

    @Test
    void surfaceProceduresAppliedAfterToothProceduresShouldCoexist () {
        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        OdontologySnomedBo action1 = new OdontologySnomedBo("action 1", "action 1");
        OdontologySnomedBo action2 = new OdontologySnomedBo("action 2", "action 2");
        OdontologySnomedBo action3 = new OdontologySnomedBo("action 3", "action 3");

        OdontologySnomedBo tooth1 = new OdontologySnomedBo("tooth 1", "tooth 1");

        dentalActions.add(new ConsultationDentalActionBo(action1, tooth1, null, false));
        dentalActions.add(new ConsultationDentalActionBo(action2, tooth1, ESurfacePosition.RIGHT, false));
        dentalActions.add(new ConsultationDentalActionBo(action3, tooth1, ESurfacePosition.LEFT, false));

        List<ToothDrawingsBo> odontogramDrawings = drawOdontogramService.run(1, dentalActions);


        Assertions.assertEquals(1, odontogramDrawings.size());

        ToothDrawingsBo toothDrawings = odontogramDrawings.get(0);
        Assertions.assertEquals("tooth 1", toothDrawings.getToothId());
        Assertions.assertEquals("action 1", toothDrawings.getWholeDrawing().getSnomed().getSctid());
        Assertions.assertNull(toothDrawings.getInternalSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getExternalSurfaceDrawing());
        Assertions.assertEquals("action 2", toothDrawings.getRightSurfaceDrawing().getSnomed().getSctid());
        Assertions.assertEquals("action 3", toothDrawings.getLeftSurfaceDrawing().getSnomed().getSctid());

    }

    @Test
    void lastToothProcedureShouldRemain () {
        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        OdontologySnomedBo action1 = new OdontologySnomedBo("action 1", "action 1");
        OdontologySnomedBo action2 = new OdontologySnomedBo("action 2", "action 2");
        OdontologySnomedBo action3 = new OdontologySnomedBo("action 3", "action 3");

        OdontologySnomedBo tooth1 = new OdontologySnomedBo("tooth 1", "tooth 1");

        dentalActions.add(new ConsultationDentalActionBo(action1, tooth1, null, false));
        dentalActions.add(new ConsultationDentalActionBo(action2, tooth1, null, false));

        List<ToothDrawingsBo> odontogramDrawings = drawOdontogramService.run(1, dentalActions);


        Assertions.assertEquals(1, odontogramDrawings.size());

        ToothDrawingsBo toothDrawings = odontogramDrawings.get(0);
        Assertions.assertEquals("tooth 1", toothDrawings.getToothId());
        Assertions.assertEquals("action 2", toothDrawings.getWholeDrawing().getSnomed().getSctid());
        Assertions.assertNull(toothDrawings.getInternalSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getExternalSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getRightSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getLeftSurfaceDrawing());

    }

    @Test
    void lastToothProcedureShouldOverlapSurfaceProcedures () {
        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        OdontologySnomedBo action1 = new OdontologySnomedBo("action 1", "action 1");
        OdontologySnomedBo action2 = new OdontologySnomedBo("action 2", "action 2");
        OdontologySnomedBo action3 = new OdontologySnomedBo("action 3", "action 3");

        OdontologySnomedBo tooth1 = new OdontologySnomedBo("tooth 1", "tooth 1");

        dentalActions.add(new ConsultationDentalActionBo(action1, tooth1, null, false));
        dentalActions.add(new ConsultationDentalActionBo(action2, tooth1, ESurfacePosition.CENTRAL, false));
        dentalActions.add(new ConsultationDentalActionBo(action3, tooth1, null, false));

        List<ToothDrawingsBo> odontogramDrawings = drawOdontogramService.run(1, dentalActions);


        Assertions.assertEquals(1, odontogramDrawings.size());

        ToothDrawingsBo toothDrawings = odontogramDrawings.get(0);
        Assertions.assertEquals("tooth 1", toothDrawings.getToothId());
        Assertions.assertEquals("action 3", toothDrawings.getWholeDrawing().getSnomed().getSctid());
        Assertions.assertNull(toothDrawings.getInternalSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getExternalSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getRightSurfaceDrawing());
        Assertions.assertNull(toothDrawings.getLeftSurfaceDrawing());

    }

    @Test
    void actionsAppliedInDifferentTeethShouldResultInSeveralDrawings () {
        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        OdontologySnomedBo action1 = new OdontologySnomedBo("action 1", "action 1");
        OdontologySnomedBo action2 = new OdontologySnomedBo("action 2", "action 2");
        OdontologySnomedBo action3 = new OdontologySnomedBo("action 3", "action 3");

        OdontologySnomedBo tooth1 = new OdontologySnomedBo("tooth 1", "tooth 1");
        OdontologySnomedBo tooth2 = new OdontologySnomedBo("tooth 2", "tooth 2");
        OdontologySnomedBo tooth3 = new OdontologySnomedBo("tooth 3", "tooth 3");

        dentalActions.add(new ConsultationDentalActionBo(action1, tooth1, null, false));
        dentalActions.add(new ConsultationDentalActionBo(action2, tooth1, ESurfacePosition.CENTRAL, false));

        dentalActions.add(new ConsultationDentalActionBo(action3, tooth2, ESurfacePosition.RIGHT, true));

        dentalActions.add(new ConsultationDentalActionBo(action3, tooth3, null, false));


        List<ToothDrawingsBo> odontogramDrawings = drawOdontogramService.run(1, dentalActions);

        Assertions.assertEquals(3, odontogramDrawings.size());

    }

}