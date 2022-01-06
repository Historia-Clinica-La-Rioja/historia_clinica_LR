package net.pladema.staff.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Setter
@Getter
@NoArgsConstructor
public class HealthcareProfessionalSpecialtyDto {

    @Nullable
    private Integer id;

    @Nullable
    private Integer healthcareProfessionalId;

    private Integer professionalSpecialtyId;

    private Integer clinicalSpecialtyId;
}
