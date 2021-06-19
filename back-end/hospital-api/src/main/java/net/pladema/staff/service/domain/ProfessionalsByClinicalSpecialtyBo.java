package net.pladema.staff.service.domain;

import lombok.*;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalsByClinicalSpecialtyBo {

    private ClinicalSpecialtyBo clinicalSpecialty;

    private List<Integer> professionalsIds;

    public ProfessionalsByClinicalSpecialtyBo(ClinicalSpecialty clinicalSpecialty, List<Integer> professionalsIds) {
        this.clinicalSpecialty = new ClinicalSpecialtyBo(clinicalSpecialty.getId(), clinicalSpecialty.getName());
        this.professionalsIds = professionalsIds;
    }

    public void addProfessionalId(Integer professionalId) {
        this.professionalsIds.add(professionalId);
    }
}
