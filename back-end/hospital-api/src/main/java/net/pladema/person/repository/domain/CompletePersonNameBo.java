package net.pladema.person.repository.domain;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import lombok.Getter;
import net.pladema.person.repository.entity.Person;

@Getter

public class CompletePersonNameBo {

	private final String nameSelfDetermination;

	private final Person person;

	private  final String personFullName;

	private final Boolean includeNameSelfDetermination = AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS.isActive();

	private final Integer healthcareProfessionalId;

	public CompletePersonNameBo(Person person, String nameSelfDetermination, Integer healthcareProfessionalId) {
		this.nameSelfDetermination = nameSelfDetermination;
		this.person = person;
		this.personFullName = this.getFullName(person.getLastName(), person.getOtherLastNames(), person.getFirstName(), person.getMiddleNames(), nameSelfDetermination);
		this.healthcareProfessionalId = healthcareProfessionalId;
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
