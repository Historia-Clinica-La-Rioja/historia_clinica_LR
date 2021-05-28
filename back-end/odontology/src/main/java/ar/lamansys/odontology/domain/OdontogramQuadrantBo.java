package ar.lamansys.odontology.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OdontogramQuadrantBo {
    private OdontologySnomedBo snomed;
    private Integer code;
    private boolean left;
    private boolean top;
    private boolean permanent;
    private List<ToothBo> teeth;

    public OdontogramQuadrantBo(
            OdontologySnomedBo snomed,
            Integer code,
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
    }

    public static OdontogramQuadrantBo getQuadrant(Integer code) {
        return OdontogramQuadrantData.getAsMap().get(code);
    }
}
