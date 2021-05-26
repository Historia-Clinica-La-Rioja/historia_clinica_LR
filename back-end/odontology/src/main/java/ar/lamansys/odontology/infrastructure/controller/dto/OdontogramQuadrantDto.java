package ar.lamansys.odontology.infrastructure.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OdontogramQuadrantDto{
    private OdontologySnomedDto snomed;
    private Integer code;
    private boolean left;
    private boolean top;
    private boolean permanent;
    private List<ToothDto> teeth;
}
