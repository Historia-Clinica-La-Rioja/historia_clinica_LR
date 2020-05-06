package net.pladema.internation.service.internment.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ResponsibleDoctorBo implements Serializable {

    private Integer id;

    private String firstName;

    private String lastName;

    private String licence;
}
