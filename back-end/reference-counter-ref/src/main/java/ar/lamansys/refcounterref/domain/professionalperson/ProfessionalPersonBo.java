package ar.lamansys.refcounterref.domain.professionalperson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalPersonBo {

    private Integer id;

    private String firstName;

	private String nameSelfDetermination;

    private String lastName;

    public ProfessionalPersonBo(String firstName, String nameSelfDetermination,
								String lastName) {
    	this.firstName = firstName;
    	this.nameSelfDetermination = nameSelfDetermination;
    	this.lastName = lastName;
	}

}
