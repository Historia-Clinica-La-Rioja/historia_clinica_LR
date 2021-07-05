package ar.lamansys.odontology.domain;

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
public class DiagnosticBo {

    private OdontologySnomedBo snomed;

    private boolean applicableToTooth;

    private boolean applicableToSurface;

    public DiagnosticBo(String sctid, String pt, boolean applicableToTooth, boolean applicableToSurface) {
        this.snomed = new OdontologySnomedBo(sctid, pt);
        this.applicableToTooth = applicableToTooth;
        this.applicableToSurface = applicableToSurface;
    }

}
