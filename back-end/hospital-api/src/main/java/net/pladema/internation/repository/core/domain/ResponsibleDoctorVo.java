package net.pladema.internation.repository.core.domain;

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
