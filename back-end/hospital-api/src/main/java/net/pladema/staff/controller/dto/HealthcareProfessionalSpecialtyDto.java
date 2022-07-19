package net.pladema.staff.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
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

	@Nullable
    private Integer professionalProfessionId;

    private ClinicalSpecialtyDto clinicalSpecialty;
}
