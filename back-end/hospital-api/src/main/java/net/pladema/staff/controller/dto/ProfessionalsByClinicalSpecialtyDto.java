package net.pladema.staff.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.dto.ClinicalSpecialtyDto;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProfessionalsByClinicalSpecialtyDto {

    private ClinicalSpecialtyDto clinicalSpecialty;

    private List<Integer> professionalsIds;

}
