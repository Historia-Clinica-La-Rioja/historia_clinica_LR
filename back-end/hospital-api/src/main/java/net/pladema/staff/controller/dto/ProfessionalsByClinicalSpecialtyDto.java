package net.pladema.staff.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProfessionalsByClinicalSpecialtyDto {

    private ClinicalSpecialtyDto clinicalSpecialty;

    private List<Integer> professionalsIds;

}
