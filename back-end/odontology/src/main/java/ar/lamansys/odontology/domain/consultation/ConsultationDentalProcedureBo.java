package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsultationDentalProcedureBo extends ClinicalTermBo {

    private OdontologySnomedBo tooth;

    private OdontologySnomedBo surface;

    public Boolean isAppliedToTooth() {
        return (this.tooth != null) && (this.surface == null);
    }

    public Boolean isAppliedToSurface() {
        return (this.tooth != null) && (this.surface != null);
    }

}
