package net.pladema.staff.controller.dto;

import lombok.*;
import net.pladema.person.controller.dto.IBasicPersonalData;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BasicPersonalDataDto implements IBasicPersonalData {

    private String firstName;

    private String lastName;

    private String identificationNumber;

    private String phoneNumber;
}
