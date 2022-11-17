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
public class HealthcareProfessionalCompleteDto {

    @Nullable
    private Integer id;

    private Integer personId;

    private List<ProfessionalProfessionsDto> professionalProfessions;

}
