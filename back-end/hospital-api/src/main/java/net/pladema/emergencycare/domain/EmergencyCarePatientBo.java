package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.person.repository.entity.Person;

@Setter
@Getter
public class EmergencyCarePatientBo {

	private Integer id;
	private Short typeId;
	private String patientDescription;
	private EmergencyCarePersonBo person;

	public EmergencyCarePatientBo(Integer id, Short typeId, Person p, String nameSelfDetermination, String identificationTypeDescription){
		this.id = id;
		this.typeId = typeId;
		if (p != null)
			this.person = new EmergencyCarePersonBo(p, nameSelfDetermination, identificationTypeDescription);
	}

}
