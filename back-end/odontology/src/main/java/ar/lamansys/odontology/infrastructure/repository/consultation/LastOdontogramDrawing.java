package ar.lamansys.odontology.infrastructure.repository.consultation;

import ar.lamansys.odontology.domain.consultation.odontogramDrawings.ToothDrawingsBo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "last_odontogram_drawing")
@NoArgsConstructor
@Getter
public class LastOdontogramDrawing {

    @EmbeddedId
    private LastOdontogramDrawingPK pk;

    @Column(name = "whole_tooth", length = 20)
    private String wholeTooth;

    @Column(name = "internal_surface", length = 20)
    private String internalSurface;

    @Column(name = "external_surface", length = 20)
    private String externalSurface;

    @Column(name = "central_surface", length = 20)
    private String centralSurface;

    @Column(name = "left_surface", length = 20)
    private String leftSurface;

    @Column(name = "right_surface", length = 20)
    private String rightSurface;

    public LastOdontogramDrawing(Integer patientId, ToothDrawingsBo toothDrawings) {
        this.pk = new LastOdontogramDrawingPK(patientId, toothDrawings.getToothId());
        this.wholeTooth = (toothDrawings.getWholeDrawing() != null) ? toothDrawings.getWholeDrawing().getSnomed().getSctid() : null;
        this.internalSurface = (toothDrawings.getInternalSurfaceDrawing() != null) ? toothDrawings.getInternalSurfaceDrawing().getSctid() : null;
        this.externalSurface = (toothDrawings.getExternalSurfaceDrawing() != null) ? toothDrawings.getExternalSurfaceDrawing().getSctid() : null;
        this.centralSurface = (toothDrawings.getCentralSurfaceDrawing() != null) ? toothDrawings.getCentralSurfaceDrawing().getSctid() : null;
        this.leftSurface = (toothDrawings.getLeftSurfaceDrawing() != null) ? toothDrawings.getLeftSurfaceDrawing().getSctid() : null;
        this.rightSurface = (toothDrawings.getRightSurfaceDrawing() != null) ? toothDrawings.getRightSurfaceDrawing().getSctid() : null;
    }

    public String getToothId() {
        return this.pk.getToothId();
    }

}
