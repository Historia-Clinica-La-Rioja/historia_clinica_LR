package ar.lamansys.odontology.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OdontogramQuadrantBo {
    private OdontologySnomedBo snomed;
    private Short code;
    private boolean left;
    private boolean top;
    private boolean permanent;
    private List<ToothBo> teeth;

    public OdontogramQuadrantBo(
            OdontologySnomedBo snomed,
            Short code,
            boolean left,
            boolean top,
            boolean permanent) {
        this.snomed = snomed;
        this.code = code;
        this.left = left;
        this.top = top;
        this.permanent = permanent;
        this.teeth = new ArrayList<>();
    }

    public void addTooth(ToothBo t) {
        this.teeth.add(t);
        this.teeth.sort(Comparator.comparing(ToothBo::getToothCode)); // ascending order by toothCode
    }

    public static OdontogramQuadrantBo getQuadrant(Short code) {
        return OdontogramQuadrantData.getAsMap().get(code);
    }
}
