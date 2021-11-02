package ar.lamansys.odontology.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
public class ToothWithPositionBo extends ToothBo {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean left;
    private boolean top;

    public ToothWithPositionBo(OdontologySnomedBo snomed, Short toothCode, Short quadrantCode, boolean isPosterior, boolean left, boolean top) {
        super(snomed, toothCode, quadrantCode, isPosterior);
        this.left = left;
        this.top = top;
    }

    public ToothWithPositionBo (ToothBo toothBo, OdontogramQuadrantBo odontogramQuadrantBo) {
        super(toothBo);
        this.left  = odontogramQuadrantBo.isLeft();
        this.top = odontogramQuadrantBo.isTop();
    }


    public ToothSurfacesBo getSurfaces() {
        ToothSurfacesBo result = new ToothSurfacesBo();

        result.setInternal(isTop() ? EToothSurfaces.PALATINA.getValue() : EToothSurfaces.VESTIBULAR.getValue());
        result.setExternal(isTop() ? EToothSurfaces.VESTIBULAR.getValue() : EToothSurfaces.LINGUAL.getValue());
        result.setRight(isLeft() ? EToothSurfaces.DISTAL.getValue() : EToothSurfaces.MESIAL.getValue());
        result.setLeft(isLeft() ? EToothSurfaces.MESIAL.getValue() : EToothSurfaces.DISTAL.getValue());
        result.setCentral(isPosterior() ? EToothSurfaces.OCLUSAL.getValue() : EToothSurfaces.INCISAL.getValue());
        logger.debug("Output -> {}", result);
        return result;
    }
}
