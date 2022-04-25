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

	private String nameSelfDetermination;

	public AuthorBo(Integer id, String firstName, String lastName, String nameSelfDetermination){
		this.id = id;
		this.firstName = firstName;
        this.lastName = lastName;
		this.nameSelfDetermination = nameSelfDetermination;
    }
}
