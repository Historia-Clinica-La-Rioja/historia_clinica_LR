package ar.lamansys.odontology.domain.consultation;

import ar.lamansys.odontology.domain.ESurfacePositionBo;
import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ECeoIndexBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ECpoIndexBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.EOdontologyIndexBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConsultationDentalActionBo extends ClinicalTermBo {

    private OdontologySnomedBo tooth;

    private OdontologySnomedBo surface;

    private ESurfacePositionBo surfacePosition;

    private ECpoIndexBo permanentIndex = ECpoIndexBo.NONE;

    private ECeoIndexBo temporaryIndex = ECeoIndexBo.NONE;

    private boolean appliedToTemporaryTooth;

    private boolean diagnostic;

    public ConsultationDentalActionBo(OdontologySnomedBo action, boolean isDiagnostic) {
        super(action);
        this.diagnostic = isDiagnostic;
    }

    public ConsultationDentalActionBo(OdontologySnomedBo action, OdontologySnomedBo tooth, ESurfacePositionBo surfacePosition, boolean isDiagnostic) {
        super(action);
        this.tooth = tooth;
        this.surfacePosition = surfacePosition;
        this.diagnostic = isDiagnostic;
    }

    public String getToothSctid() {
        return this.tooth.getSctid();
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

    public EOdontologyIndexBo getIndex() {
        if (this.appliedToTemporaryTooth)
            switch (temporaryIndex) {
                case C:
                    return EOdontologyIndexBo.CAVITIES;
                case E:
                    return EOdontologyIndexBo.LOST;
                case O:
                    return EOdontologyIndexBo.FIXED;
                default:
                    return EOdontologyIndexBo.NONE;
            }
        switch (permanentIndex) {
            case C:
                return EOdontologyIndexBo.CAVITIES;
            case P:
                return EOdontologyIndexBo.LOST;
            case O:
                return EOdontologyIndexBo.FIXED;
            default:
                return EOdontologyIndexBo.NONE;
        }
    }

}
