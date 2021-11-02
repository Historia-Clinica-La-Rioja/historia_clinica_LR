package ar.lamansys.odontology.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToothBo {
    private OdontologySnomedBo snomed;
    private Short toothCode;
    private Short quadrantCode;
    private boolean posterior;
    private final Logger logger =  LoggerFactory.getLogger(getClass());

    public ToothBo(ToothBo other){
        this.snomed = other.snomed;
        this.toothCode = other.toothCode;
        this.quadrantCode = other.quadrantCode;
        this.posterior = other.posterior;
    }

    public boolean belongsToQuadrant(Short quadrantCode) {
        logger.debug("Input -> {}", quadrantCode);
        var result = quadrantCode.equals(this.getQuadrantCode());
        logger.debug("Output -> {}", result);
        return result;
    }

    public boolean isTemporary() {
        return quadrantCode >= 5 && quadrantCode <= 8;
    }

    public boolean isPermanent() {
        return quadrantCode >= 1 && quadrantCode <= 4;
    }

}
