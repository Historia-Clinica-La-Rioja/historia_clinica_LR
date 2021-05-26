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
    private Integer toothCode;
    private Integer quadrantCode;
    private boolean isPosterior;
    private final Logger LOG =  LoggerFactory.getLogger(getClass());

    public boolean belongsToQuadrant(Integer quadrantCode) {
        LOG.debug("Input -> {}", quadrantCode);
        var result = quadrantCode.equals(this.getQuadrantCode());
        LOG.debug("Output -> {}", result);
        return result;
    }
}
