package net.pladema.clinichistory.hospitalization.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PatientDto {

    private Integer id;

    private String firstName;

    private String lastName;
}
