package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.domain.ESurfacePosition;
import ar.lamansys.odontology.domain.OdontologySnomedBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConsultationDentalActionBo extends ClinicalTermBo {

    private OdontologySnomedBo tooth;

    private OdontologySnomedBo surface;

    private ESurfacePosition surfacePosition;

    private boolean diagnostic;

    public ConsultationDentalActionBo(OdontologySnomedBo action, boolean isDiagnostic) {
        super(action);
        this.diagnostic = isDiagnostic;
    }

    public ConsultationDentalActionBo(OdontologySnomedBo action, OdontologySnomedBo tooth, ESurfacePosition surfacePosition, boolean isDiagnostic) {
        super(action);
        this.tooth = tooth;
        this.surfacePosition = surfacePosition;
        this.diagnostic = isDiagnostic;
    }

    public boolean isProcedure() {
        return !this.diagnostic;
    }

    public boolean isAppliedToTooth() {
        return (this.tooth != null) && (this.surfacePosition == null);
    }

    public boolean isAppliedToSurface() {
        return (this.tooth != null) && (this.surfacePosition != null);
    }

}
