package net.pladema.person.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BasicDataPersonDto {

    private Integer id;

    private String firstName;

    private String middleNames;

    private String lastName;

    private String otherLastNames;

    private GenderDto gender;

    private Short age;
}
