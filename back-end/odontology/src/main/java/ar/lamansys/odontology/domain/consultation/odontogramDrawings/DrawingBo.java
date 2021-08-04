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

    private OdontologySnomedBo snomedBo;

    private boolean diagnostic;

    public boolean isProcedure () {
        return !diagnostic;
    }

}
