package ar.lamansys.odontology.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToothDto {
    private OdontologySnomedDto snomed;
    private Integer code;
}
