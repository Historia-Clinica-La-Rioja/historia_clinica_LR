package ar.lamansys.odontology.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ToothSurfacesDto {
    private OdontologySnomedDto left;
    private OdontologySnomedDto right;
    private OdontologySnomedDto internal;
    private OdontologySnomedDto external;
    private OdontologySnomedDto central;
}
