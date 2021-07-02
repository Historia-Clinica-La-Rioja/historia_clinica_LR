package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProfessionalInfoDto {

    private final Integer id;

    private final List<ClinicalSpecialtyDto> clinicalSpecialties;

    public ProfessionalInfoDto(Integer id, List<ClinicalSpecialtyDto> clinicalSpecialties) {
        this.id = id;
        this.clinicalSpecialties = clinicalSpecialties;
    }
}
