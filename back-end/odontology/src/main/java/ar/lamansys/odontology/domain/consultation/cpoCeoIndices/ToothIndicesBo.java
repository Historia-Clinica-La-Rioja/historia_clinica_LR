package ar.lamansys.odontology.domain.consultation.cpoCeoIndices;

import ar.lamansys.odontology.domain.ESurfacePositionBo;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToothIndicesBo {

    private String toothId;

    private boolean temporary;

    private EOdontologyIndexBo wholeToothIndex;

    private EOdontologyIndexBo internalIndex;

    private EOdontologyIndexBo externalIndex;

    private EOdontologyIndexBo leftIndex;

    private EOdontologyIndexBo rightIndex;

    private EOdontologyIndexBo centralIndex;

    public ToothIndicesBo(String toothId, boolean isTemporary) {
        this.toothId = toothId;
        this.temporary = isTemporary;
        this.setWholeToothIndex(EOdontologyIndexBo.NONE);
        this.setCentralIndex(EOdontologyIndexBo.NONE);
        this.setInternalIndex(EOdontologyIndexBo.NONE);
        this.setExternalIndex(EOdontologyIndexBo.NONE);
        this.setLeftIndex(EOdontologyIndexBo.NONE);
        this.setRightIndex(EOdontologyIndexBo.NONE);
    }

    public void apply(ConsultationDentalActionBo dentalAction) {
        if (dentalAction.isAppliedToTooth()) {
            EOdontologyIndexBo inputToothState = dentalAction.getIndex();
            setWholeToothIndex(computeNewState(this.getWholeToothIndex(), inputToothState));
            return;
        }
        EOdontologyIndexBo inputSurfaceState = dentalAction.getIndex();
        ESurfacePositionBo surfacePosition = dentalAction.getSurfacePosition();
        switch (surfacePosition) {
            case INTERNAL:
                setInternalIndex(computeNewState(this.getInternalIndex(), inputSurfaceState));
                break;
            case EXTERNAL:
                setExternalIndex(computeNewState(this.getExternalIndex(), inputSurfaceState));
                break;
            case LEFT:
                setLeftIndex(computeNewState(this.getLeftIndex(), inputSurfaceState));
                break;
            case RIGHT:
                setRightIndex(computeNewState(this.getRightIndex(), inputSurfaceState));
                break;
            case CENTRAL:
                setCentralIndex(computeNewState(this.getCentralIndex(), inputSurfaceState));
                break;

        }
    }

    private EOdontologyIndexBo computeNewState(EOdontologyIndexBo currentState, EOdontologyIndexBo inputState) {
        // if the current state is "lost", that state doesn't change
        if (EOdontologyIndexBo.LOST.equals(currentState))
            return EOdontologyIndexBo.LOST;

        // if the input state applies to none of the indices, the current state remains the same
        if (EOdontologyIndexBo.NONE.equals(inputState))
            return currentState;

        // in any other case, the input state should be updated
        return inputState;
    }

    public EOdontologyIndexBo computeToothResultingIndex() {
        // if the whole tooth state applies to a type of index, return that one
        if (!EOdontologyIndexBo.NONE.equals(this.getWholeToothIndex()))
            return this.getWholeToothIndex();
        // if not, check the surfaces
        return computeToothResultingStateBySurfaces();
    }

    private EOdontologyIndexBo computeToothResultingStateBySurfaces() {
        int c = 0;
        int o = 0;

        if (EOdontologyIndexBo.CAVITIES.equals(getInternalIndex())) c++;
        if (EOdontologyIndexBo.CAVITIES.equals(getExternalIndex())) c++;
        if (EOdontologyIndexBo.CAVITIES.equals(getLeftIndex()))     c++;
        if (EOdontologyIndexBo.CAVITIES.equals(getRightIndex()))    c++;
        if (EOdontologyIndexBo.CAVITIES.equals(getCentralIndex()))  c++;

        if (EOdontologyIndexBo.FIXED.equals(getInternalIndex())) o++;
        if (EOdontologyIndexBo.FIXED.equals(getExternalIndex())) o++;
        if (EOdontologyIndexBo.FIXED.equals(getLeftIndex()))     o++;
        if (EOdontologyIndexBo.FIXED.equals(getRightIndex()))    o++;
        if (EOdontologyIndexBo.FIXED.equals(getCentralIndex()))  o++;

        if (o == 0 && c == 0)
            return EOdontologyIndexBo.NONE;
        if (c <= o)
            return EOdontologyIndexBo.FIXED;
        else // c > o
            return EOdontologyIndexBo.CAVITIES;
    }

}
