package ar.lamansys.odontology.domain;

import lombok.*;

@Getter
@Setter
@ToString
public class OdontogramQuadrantBo {
    private OdontologySnomedBo snomed;
    private Integer quadrantCode;
}
