package net.pladema.internation.service.internment.summary.domain;

import lombok.*;
import net.pladema.internation.repository.internment.domain.summary.ResponsibleDoctorVo;

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
