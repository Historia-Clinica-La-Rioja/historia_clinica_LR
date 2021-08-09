package ar.lamansys.odontology.domain.consultation.odontogramDrawings;

import ar.lamansys.odontology.domain.ESurfacePositionBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ToothDrawingsBo {

    private String toothId;

    private DrawingBo wholeDrawing;

    private DrawingBo internalSurfaceDrawing;

    private DrawingBo externalSurfaceDrawing;

    private DrawingBo leftSurfaceDrawing;

    private DrawingBo rightSurfaceDrawing;

    private DrawingBo centralSurfaceDrawing;

    public ToothDrawingsBo(String toothId) {
        this.toothId = toothId;
    }

    private void eraseAllDrawings() {
        this.wholeDrawing = null;
        this.internalSurfaceDrawing = null;
        this.externalSurfaceDrawing = null;
        this.leftSurfaceDrawing = null;
        this.rightSurfaceDrawing = null;
    }

    public void draw(ConsultationDentalActionBo action) {
        if (action.isAppliedToSurface()) {
            DrawingBo surfaceDrawing = getSurfaceDrawing(action.getSurfacePosition());
            if (!action.isProcedure() && this.wholeToothDrawingHasProcedure())
                return;
            if (surfaceDrawing == null || surfaceDrawing.isProcedure()) {
                updateSurfaceDrawing(action);
            }
        } else { // is applied to tooth
            if (action.isProcedure()) {
                eraseAllDrawings();
                wholeDrawing = new DrawingBo(action.getSnomed(), action.isDiagnostic());
            } else if (wholeDrawing == null)
                wholeDrawing = new DrawingBo(action.getSnomed(), action.isDiagnostic());
        }
    }

    private DrawingBo getSurfaceDrawing(ESurfacePositionBo surfacePosition) {
        switch (surfacePosition) {
            case INTERNAL:
                return this.internalSurfaceDrawing;
            case EXTERNAL:
                return this.externalSurfaceDrawing;
            case LEFT:
                return this.leftSurfaceDrawing;
            case RIGHT:
                return this.rightSurfaceDrawing;
            case CENTRAL:
                return this.centralSurfaceDrawing;
            default:
                return null;
        }
    }

    private void updateSurfaceDrawing(ConsultationDentalActionBo action) {
        switch (action.getSurfacePosition()) {
            case INTERNAL:
                this.internalSurfaceDrawing = new DrawingBo(action.getSnomed(), action.isDiagnostic());
                break;
            case EXTERNAL:
                this.externalSurfaceDrawing = new DrawingBo(action.getSnomed(), action.isDiagnostic());
                break;
            case LEFT:
                this.leftSurfaceDrawing = new DrawingBo(action.getSnomed(), action.isDiagnostic());
                break;
            case RIGHT:
                this.rightSurfaceDrawing = new DrawingBo(action.getSnomed(), action.isDiagnostic());
                break;
            case CENTRAL:
                this.centralSurfaceDrawing = new DrawingBo(action.getSnomed(), action.isDiagnostic());
                break;
        }
    }

    private boolean wholeToothDrawingHasProcedure() {
        return (this.wholeDrawing != null) && (this.wholeDrawing.isProcedure());
    }

}
