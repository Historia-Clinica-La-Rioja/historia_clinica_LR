package net.pladema.clinichistory.hospitalization.service.summary.domain;

import lombok.*;
import net.pladema.clinichistory.hospitalization.repository.domain.summary.ResponsibleDoctorVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleDoctorBo {

    private Integer id;

    private String firstName;

    private String lastName;

    private String licence;

    public ResponsibleDoctorBo(ResponsibleDoctorVo responsibleDoctorVo){
        super();
        this.id = responsibleDoctorVo.getId();
        this.firstName = responsibleDoctorVo.getFirstName();
        this.lastName = responsibleDoctorVo.getLastName();
        this.licence = responsibleDoctorVo.getLicence();
    }

    public ResponsibleDoctorBo(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
