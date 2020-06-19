package net.pladema.clinichistory.hospitalization.repository.domain.summary;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponsibleDoctorVo {

    private Integer id;

    private String firstName;

    private String lastName;

    private String licence;
}
