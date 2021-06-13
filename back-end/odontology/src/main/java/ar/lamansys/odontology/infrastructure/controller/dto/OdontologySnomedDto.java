package ar.lamansys.odontology.infrastructure.controller.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OdontologySnomedDto {
    private Integer id;
    private String sctid;
    private String pt;
}
