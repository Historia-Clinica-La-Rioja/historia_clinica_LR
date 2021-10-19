package ar.lamansys.sgh.publicapi.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfessionalBo {

    private Integer id;
    private String firstName;
    private String lastName;
    private String licenceNumber;
    private String indentificationNumber;
}
