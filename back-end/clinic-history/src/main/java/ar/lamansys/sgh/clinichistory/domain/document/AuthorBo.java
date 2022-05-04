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

    public AuthorBo(Integer id, String firstName, String lastName){
    	this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
