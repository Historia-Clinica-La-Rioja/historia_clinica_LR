package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConsultationDentalDiagnosticBo extends ClinicalTermBo {

    private OdontologySnomedBo tooth;

    private OdontologySnomedBo surface;

    public ConsultationDentalDiagnosticBo(OdontologySnomedBo diagnostic) {
        super(diagnostic);
    }

    public ConsultationDentalDiagnosticBo(OdontologySnomedBo diagnostic, OdontologySnomedBo tooth, OdontologySnomedBo surface) {
        super(diagnostic);
        this.tooth = tooth;
        this.surface = surface;
    }

    public Boolean isAppliedToTooth() {
        return (this.tooth != null) && (this.surface == null);
    }

    public Boolean isAppliedToSurface() {
        return (this.tooth != null) && (this.surface != null);
    }

}
