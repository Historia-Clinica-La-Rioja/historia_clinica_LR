package net.pladema.internation.service.internment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PatientBo implements Serializable {

    private Integer id;

    private String firstName;

    private String lastName;
}
