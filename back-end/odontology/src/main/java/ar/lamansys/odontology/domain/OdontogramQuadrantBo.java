package ar.lamansys.odontology.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OdontogramQuadrantBo {
    private OdontologySnomedBo snomed;
    private Integer code;
    private boolean isLeft;
    private boolean isTop;
    private boolean isPermanent;
    private List<ToothBo> teeth;
}
