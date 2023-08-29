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

	public String getFullName(boolean ffIsOn) {

		StringBuilder fullNameBuilder = new StringBuilder();

		if (ffIsOn && this.nameSelfDetermination != null) {
			fullNameBuilder.append(this.lastName)
					.append(" ")
					.append(this.nameSelfDetermination);
		} else {
			fullNameBuilder.append(this.lastName);

			if (this.otherLastNames != null) {
				fullNameBuilder.append(" ")
						.append(this.otherLastNames);
			}

			fullNameBuilder.append(" ")
					.append(this.firstName);

			if (this.middleNames != null) {
				fullNameBuilder.append(" ")
						.append(this.middleNames);
			}
		}

		return fullNameBuilder.toString();
	}

}
