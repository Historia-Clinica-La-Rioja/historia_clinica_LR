package ar.lamansys.sgh.publicapi.activities.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonInfoBo {

    private String identificationNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private GenderEnum gender;
}
