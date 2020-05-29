package net.pladema.patient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.controller.dto.AAdditionalDoctorDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorsBo {

    private AdditionalDoctorBo generalPractitionerBo;
    private AdditionalDoctorBo pamiDoctorBo;

    public DoctorsBo (AAdditionalDoctorDto generalPractitionerDto,AAdditionalDoctorDto pamiDoctorDto){ // From FE to BE
        this.generalPractitionerBo = new AdditionalDoctorBo(generalPractitionerDto);
        this.pamiDoctorBo = new AdditionalDoctorBo(pamiDoctorDto);
    }


}
