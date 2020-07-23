package net.pladema.staff.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BasicPersonalDataDto {

    private String firstName;

    private String lastName;

    private String identificationNumber;
}
