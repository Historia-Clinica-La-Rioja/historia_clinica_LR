package ar.lamansys.odontology.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TeethGroupBo {
    private OdontogramQuadrantBo quadrant;
    private List<ToothBo> teeth;

    public TeethGroupBo(OdontogramQuadrantBo quadrant,
                        List<ToothBo> teeth){
        this.quadrant = quadrant;
        this.teeth = teeth;
        Collections.sort(this.teeth);
        this.reverseTeethIfNeeded();
    }

    private int getActualQuadrant() {
        return ((quadrant.getQuadrantCode() - 1) % 4 ) + 1;
    }

    private void reverseTeethIfNeeded() {
        int actualQuadrant = getActualQuadrant();
        if (actualQuadrant == 1 || actualQuadrant == 4) {
            Collections.reverse(this.teeth);
        }
    }
}
