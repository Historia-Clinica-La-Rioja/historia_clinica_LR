package net.pladema.staff.controller.dto;

import java.util.List;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalProfessionsDto {

    @Nullable
    private Integer id;

	@Nullable
    private Integer healthcareProfessionalId;

    private ProfessionalSpecialtyDto profession;

	private List<HealthcareProfessionalSpecialtyDto> specialties;

}
