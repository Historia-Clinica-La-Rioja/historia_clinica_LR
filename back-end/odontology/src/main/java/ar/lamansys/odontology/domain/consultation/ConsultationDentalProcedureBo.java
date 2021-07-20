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
public class ConsultationDentalProcedureBo extends ClinicalTermBo {

    private OdontologySnomedBo tooth;

    private OdontologySnomedBo surface;

    public ConsultationDentalProcedureBo(OdontologySnomedBo procedure) {
        super(procedure);
    }

    public ConsultationDentalProcedureBo(OdontologySnomedBo procedure, OdontologySnomedBo tooth, OdontologySnomedBo surface) {
        super(procedure);
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
