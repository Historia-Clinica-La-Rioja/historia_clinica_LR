package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HealthcareProfessionalBo {

    private Integer id;

    private CHPersonBo person;

    private String licenseNumber;

    public HealthcareProfessionalBo(Integer id, String licenseNumber,
                                    Integer personId, String firstName,
                                    String lastName, String identificationNumber, String nameSelfDetermination,String middleNames,String otherLastNames) {
        this.person = new CHPersonBo(personId, firstName, lastName, identificationNumber, nameSelfDetermination,middleNames,otherLastNames);
        this.id = id;
        this.licenseNumber = licenseNumber;
    }

	public HealthcareProfessionalBo(Integer id){
		this.id = id;
	}
}
