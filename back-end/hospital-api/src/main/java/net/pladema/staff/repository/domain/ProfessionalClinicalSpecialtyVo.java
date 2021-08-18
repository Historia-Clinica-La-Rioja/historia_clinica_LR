package net.pladema.staff.repository.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@Getter
@ToString
@Value
public class ProfessionalClinicalSpecialtyVo {

    private Integer professionalId;

    private ClinicalSpecialty clinicalSpecialty;

    public ProfessionalClinicalSpecialtyVo(Integer professionalId, ClinicalSpecialty clinicalSpecialty) {
        this.professionalId = professionalId;
        clinicalSpecialty.fixSpecialtyType();
        this.clinicalSpecialty = clinicalSpecialty;
    }

    public boolean isSpecialty() {
        return clinicalSpecialty.isSpecialty();
    }
}
