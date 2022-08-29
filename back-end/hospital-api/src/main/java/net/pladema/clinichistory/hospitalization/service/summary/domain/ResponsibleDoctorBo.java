package net.pladema.clinichistory.hospitalization.service.summary.domain;

import java.util.List;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.ResponsibleDoctorVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleDoctorBo {

    private Integer id;

    private String firstName;

    private String lastName;

    private List<String> licenses;

    public ResponsibleDoctorBo(ResponsibleDoctorVo responsibleDoctorVo){
        super();
        this.id = responsibleDoctorVo.getId();
        this.firstName = responsibleDoctorVo.getFirstName();
        this.lastName = responsibleDoctorVo.getLastName();
        this.licenses = responsibleDoctorVo.getLicenses();
    }

    public ResponsibleDoctorBo(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
