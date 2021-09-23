package ar.lamansys.odontology.domain;

import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ECeoIndexBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ECpoIndexBo;
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

    private ECpoIndexBo permanentIndex = ECpoIndexBo.NONE;

    private ECeoIndexBo temporaryIndex = ECeoIndexBo.NONE;

    public DiagnosticBo(String sctid, String pt, boolean applicableToTooth, boolean applicableToSurface) {
        this.snomed = new OdontologySnomedBo(sctid, pt);
        this.applicableToTooth = applicableToTooth;
        this.applicableToSurface = applicableToSurface;
    }

    public DiagnosticBo(String sctid, String pt, boolean applicableToTooth, boolean applicableToSurface,
                        boolean permanentC, boolean permanentP, boolean permanentO,
                        boolean temporaryC, boolean temporaryE, boolean temporaryO) {
        this.snomed = new OdontologySnomedBo(sctid, pt);
        this.applicableToTooth = applicableToTooth;
        this.applicableToSurface = applicableToSurface;
        if (permanentC) this.permanentIndex = ECpoIndexBo.C;
        if (permanentP) this.permanentIndex = ECpoIndexBo.P;
        if (permanentO) this.permanentIndex = ECpoIndexBo.O;
        if (temporaryC) this.temporaryIndex = ECeoIndexBo.C;
        if (temporaryE) this.temporaryIndex = ECeoIndexBo.E;
        if (temporaryO) this.temporaryIndex = ECeoIndexBo.O;
    }

}
