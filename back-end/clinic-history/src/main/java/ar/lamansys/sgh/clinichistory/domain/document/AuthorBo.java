package ar.lamansys.sgh.clinichistory.domain.document;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthorBo {

    private Integer id;

    private String firstName;

    private String lastName;

    private String licence;

    public AuthorBo(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
