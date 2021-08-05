package ar.lamansys.odontology.domain.consultation.odontogramDrawings;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
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

    private boolean diagnostic;

    public String getSctid() {
        return this.snomed != null ? this.snomed.getSctid() : null;
    }

    public boolean isProcedure () {
        return !diagnostic;
    }

}
