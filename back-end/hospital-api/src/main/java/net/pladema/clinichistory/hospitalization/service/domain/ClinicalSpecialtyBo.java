package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.repository.domain.ClinicalSpecialtyVo;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ClinicalSpecialtyBo implements Serializable {

    private Integer id;

    private String name;

    public ClinicalSpecialtyBo(ClinicalSpecialtyVo clinicalSpecialty){
        if(clinicalSpecialty != null) {
            this.id = clinicalSpecialty.getId();
            this.name = clinicalSpecialty.getName();
        }
    }
}
