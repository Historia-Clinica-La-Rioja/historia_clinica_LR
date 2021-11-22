package ar.lamansys.odontology.domain.consultation.odontogramDrawings;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DrawingBo {

    private OdontologySnomedBo snomed;

    private EDrawingTypeBo drawingType = EDrawingTypeBo.RECORD;

    public DrawingBo(String sctid) {
        this.snomed = new OdontologySnomedBo(sctid, null);
    }

    public DrawingBo(ConsultationDentalActionBo action) {
        this.snomed = action.getSnomed();
        this.drawingType = action.isDiagnostic() ? EDrawingTypeBo.DIAGNOSTIC : EDrawingTypeBo.PROCEDURE;
    }

    public String getSctid() {
        return this.snomed != null ? this.snomed.getSctid() : null;
    }

    public boolean isProcedure () {
        return EDrawingTypeBo.PROCEDURE.equals(drawingType);
    }

    public boolean isDiagnostic () {
        return EDrawingTypeBo.DIAGNOSTIC.equals(drawingType);
    }

    public boolean isRecord () {
        return EDrawingTypeBo.RECORD.equals(drawingType);
    }

}
