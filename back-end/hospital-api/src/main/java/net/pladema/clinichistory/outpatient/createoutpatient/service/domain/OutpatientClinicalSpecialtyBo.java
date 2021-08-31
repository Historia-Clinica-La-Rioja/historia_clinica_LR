package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

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
public class OutpatientClinicalSpecialtyBo implements Serializable {

    private Integer id;

    private String name;

    public OutpatientClinicalSpecialtyBo(ClinicalSpecialtyVo clinicalSpecialty){
        if(clinicalSpecialty != null) {
            this.id = clinicalSpecialty.getId();
            this.name = clinicalSpecialty.getName();
        }
    }
}
