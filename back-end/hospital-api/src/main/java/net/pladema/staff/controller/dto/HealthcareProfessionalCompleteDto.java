package net.pladema.staff.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthcareProfessionalCompleteDto {

    @Nullable
    private Integer id;

    private Integer personId;

    private String licenseNumber;

    private List<HealthcareProfessionalSpecialtyDto> professionalSpecialtyDtos;

}
