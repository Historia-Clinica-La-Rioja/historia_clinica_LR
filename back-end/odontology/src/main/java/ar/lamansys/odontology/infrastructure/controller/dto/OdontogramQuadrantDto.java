package ar.lamansys.odontology.infrastructure.controller.dto;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ToothBo;
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
    private boolean isLeft;
    private boolean isTop;
    private boolean isPermanent;
    private List<ToothDto> teeth;
}
