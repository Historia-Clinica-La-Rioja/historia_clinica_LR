package net.pladema.staff.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class HealthcareProfessionalSpecialtyDto {

    private Integer id;

    private Integer healthcareProfessionalId;

    private Integer professionalSpecialtyId;

    private Integer clinicalSpecialtyId;
}
