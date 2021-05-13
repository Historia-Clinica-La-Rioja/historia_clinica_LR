package ar.lamansys.odontology.infrastructure.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OdontogramQuadrantDto{
    private OdontologySnomedDto snomed;
    private Integer quadrantCode;
}
