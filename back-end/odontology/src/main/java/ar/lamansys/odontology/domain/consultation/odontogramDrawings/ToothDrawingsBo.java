package ar.lamansys.odontology.domain.consultation.odontogramDrawings;

import ar.lamansys.odontology.domain.ESurfacePositionBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.infrastructure.repository.consultation.LastOdontogramDrawing;
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

    public ToothDrawingsBo(LastOdontogramDrawing lastOdontogramDrawing) {
        this.toothId = lastOdontogramDrawing.getToothId();
        if (lastOdontogramDrawing.getWholeTooth() != null)
            this.wholeDrawing = new DrawingBo(lastOdontogramDrawing.getWholeTooth());
        if (lastOdontogramDrawing.getInternalSurface() != null)
            this.internalSurfaceDrawing = new DrawingBo(lastOdontogramDrawing.getInternalSurface());
        if (lastOdontogramDrawing.getExternalSurface() != null)
            this.externalSurfaceDrawing = new DrawingBo(lastOdontogramDrawing.getExternalSurface());
        if (lastOdontogramDrawing.getCentralSurface() != null)
            this.centralSurfaceDrawing = new DrawingBo(lastOdontogramDrawing.getCentralSurface());
        if (lastOdontogramDrawing.getLeftSurface() != null)
            this.leftSurfaceDrawing = new DrawingBo(lastOdontogramDrawing.getLeftSurface());
        if (lastOdontogramDrawing.getRightSurface() != null)
            this.rightSurfaceDrawing = new DrawingBo(lastOdontogramDrawing.getRightSurface());
    }

    private void eraseAllDrawings() {
        this.wholeDrawing = null;
        this.internalSurfaceDrawing = null;
        this.externalSurfaceDrawing = null;
        this.leftSurfaceDrawing = null;
        this.rightSurfaceDrawing = null;
        this.centralSurfaceDrawing = null;
    }

    public void draw(ConsultationDentalActionBo action) {
        if (hasAnyRecordDrawing())
            eraseAllAndApplyDrawing(action);
        else if (action.isAppliedToSurface())
            applySurfaceDrawing(action);
        else
            applyWholeToothDrawing(action);
    }

    private void eraseAllAndApplyDrawing(ConsultationDentalActionBo action) {
        eraseAllDrawings();
        if (action.isAppliedToSurface())
            updateSurfaceDrawing(action);
        else
            this.wholeDrawing = new DrawingBo(action);
    }

    private void applyWholeToothDrawing(ConsultationDentalActionBo action) {
        if (action.isProcedure()) {
            eraseAllDrawings();
            this.wholeDrawing = new DrawingBo(action);
        } else if (wholeDrawing == null)
            this.wholeDrawing = new DrawingBo(action);
    }

    private void applySurfaceDrawing(ConsultationDentalActionBo action) {
        DrawingBo currentSurfaceDrawing = getSurfaceDrawing(action.getSurfacePosition());
        if (action.isDiagnostic() && this.wholeToothDrawingHasProcedure())
            return;
        if (currentSurfaceDrawing == null || !currentSurfaceDrawing.isProcedure()) {
            updateSurfaceDrawing(action);
        }
    }

    private boolean hasAnyRecordDrawing() {
        return (wholeDrawing != null && wholeDrawing.isRecord())
                || (internalSurfaceDrawing != null && internalSurfaceDrawing.isRecord())
                || (externalSurfaceDrawing != null && externalSurfaceDrawing.isRecord())
                || (leftSurfaceDrawing != null && leftSurfaceDrawing.isRecord())
                || (rightSurfaceDrawing != null && rightSurfaceDrawing.isRecord())
                || (centralSurfaceDrawing != null && centralSurfaceDrawing.isRecord());
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
                this.internalSurfaceDrawing = new DrawingBo(action);
                break;
            case EXTERNAL:
                this.externalSurfaceDrawing = new DrawingBo(action);
                break;
            case LEFT:
                this.leftSurfaceDrawing = new DrawingBo(action);
                break;
            case RIGHT:
                this.rightSurfaceDrawing = new DrawingBo(action);
                break;
            case CENTRAL:
                this.centralSurfaceDrawing = new DrawingBo(action);
                break;
        }
    }

    private boolean wholeToothDrawingHasProcedure() {
        return (this.wholeDrawing != null) && (this.wholeDrawing.isProcedure());
    }

    public boolean hasAnyDrawing() {
        return (this.wholeDrawing != null) ||
                (this.internalSurfaceDrawing != null) ||
                (this.externalSurfaceDrawing != null) ||
                (this.centralSurfaceDrawing != null) ||
                (this.leftSurfaceDrawing != null) ||
                (this.rightSurfaceDrawing != null);
    }

    public String getWholeToothSctid() {
        return (this.wholeDrawing != null) ? this.wholeDrawing.getSctid() : null;
    }

    public String getInternalSctid() {
        return (this.internalSurfaceDrawing != null) ? this.internalSurfaceDrawing.getSctid() : null;
    }

    public String getExternalSctid() {
        return (this.externalSurfaceDrawing != null) ? this.externalSurfaceDrawing.getSctid() : null;
    }

    public String getCentralSctid() {
        return (this.centralSurfaceDrawing != null) ? this.centralSurfaceDrawing.getSctid() : null;
    }

    public String getLeftSctid() {
        return (this.leftSurfaceDrawing != null) ? this.leftSurfaceDrawing.getSctid() : null;
    }

    public String getRightSctid() {
        return (this.rightSurfaceDrawing != null) ? this.rightSurfaceDrawing.getSctid() : null;
    }

}
