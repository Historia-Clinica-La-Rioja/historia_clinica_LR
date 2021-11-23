package net.pladema.staff.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareProfessionalSpecialtyBo {

    private Integer id;

    private Integer healthcareProfessionalId;

    private Integer professionalSpecialtyId;

    private Integer clinicalSpecialtyId;

    public HealthcareProfessionalSpecialtyBo(Integer healthcareProfessionalId,
                                             Integer professionalSpecialtyId,
                                             Integer clinicalSpecialtyId){
        this.healthcareProfessionalId = healthcareProfessionalId;
        this.professionalSpecialtyId = professionalSpecialtyId;
        this.clinicalSpecialtyId = clinicalSpecialtyId;
    }
}
