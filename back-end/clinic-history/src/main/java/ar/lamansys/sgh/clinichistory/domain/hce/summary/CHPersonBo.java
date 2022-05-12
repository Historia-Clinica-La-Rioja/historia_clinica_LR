package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CHPersonBo {

    private Integer id;

    private String firstName;

    private String lastName;

    private String identificationNumber;

	private String nameSelfDetermination;
    
    public CHPersonBo(Integer id, String firstName, String lastName, String identificationNumber, String nameSelfDetermination) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identificationNumber = identificationNumber;
		this.nameSelfDetermination = nameSelfDetermination;
	}
}
