package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
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

    private String identificationNumber;

	private String fullName;

	private Boolean includeNameSelfDetermination = AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS.isActive();
    
    public CHPersonBo(Integer id, String firstName, String lastName, String identificationNumber, String nameSelfDetermination,String middleNames,String otherLastNames) {
        this.id = id;
        this.identificationNumber = identificationNumber;
		this.fullName = this.getFullName(lastName, otherLastNames, firstName, middleNames, nameSelfDetermination);

	}

	public String getFullName(String doctorLastName, String doctorOtherLastNames,
									String doctorFirstName, String doctorMiddleNames, String nameSelfDetermination){
		String fullName = doctorLastName;
		if(!(doctorOtherLastNames == null || doctorOtherLastNames.isBlank()))
			fullName += " " + doctorOtherLastNames;
		if(includeNameSelfDetermination && !(nameSelfDetermination == null || nameSelfDetermination.isBlank()))
			fullName += " " + nameSelfDetermination;
		else
			fullName += " " + doctorFirstName;
		if(!(includeNameSelfDetermination) && !(doctorMiddleNames == null || doctorMiddleNames.isBlank()))
			fullName += " " + doctorMiddleNames;
		return fullName;
	}
}
