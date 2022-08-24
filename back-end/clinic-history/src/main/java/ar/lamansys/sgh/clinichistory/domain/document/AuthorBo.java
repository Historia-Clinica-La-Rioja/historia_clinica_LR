package ar.lamansys.sgh.clinichistory.domain.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AuthorBo {

	private Integer userId;
    private String firstName;
    private String lastName;
	private String nameSelfDetermination;

	public AuthorBo(Integer userId, String firstName, String lastName, String nameSelfDetermination){
		this.userId = userId;
		this.firstName = firstName;
        this.lastName = lastName;
		this.nameSelfDetermination = nameSelfDetermination;
    }

}
