package ar.lamansys.odontology.domain.consultation;

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

    private boolean diagnostic;

    public ConsultationDentalActionBo(OdontologySnomedBo action, boolean isDiagnostic) {
        super(action);
        this.diagnostic = isDiagnostic;
    }

    public ConsultationDentalActionBo(OdontologySnomedBo action, OdontologySnomedBo tooth, OdontologySnomedBo surface, boolean isDiagnostic) {
        super(action);
        this.tooth = tooth;
        this.surface = surface;
        this.diagnostic = isDiagnostic;
    }

    public boolean isProcedure() {
        return !this.diagnostic;
    }

    public Boolean isAppliedToTooth() {
        return (this.tooth != null) && (this.surface == null);
    }

    public Boolean isAppliedToSurface() {
        return (this.tooth != null) && (this.surface != null);
    }

}
