package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.domain.ProfessionalPersonVo;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalPersonBo {

	private Integer id;

	private String firstName;

	private String lastName;

	private String nameSelfDetermination;

	private String middleNames;

	private String otherLastNames;

	public ProfessionalPersonBo(Integer id, String firstName, String lastName, String nameSelfDetermination) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nameSelfDetermination = nameSelfDetermination;
	}

	public ProfessionalPersonBo(ProfessionalPersonVo professionalPerson) {
		this.id = professionalPerson.getId();
		this.firstName = professionalPerson.getFirstName();
		this.lastName = professionalPerson.getLastName();
		this.nameSelfDetermination = professionalPerson.getNameSelfDetermination();
		this.middleNames = professionalPerson.getMiddleNames();
		this.otherLastNames = professionalPerson.getOtherLastNames();
	}
}
