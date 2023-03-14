package net.pladema.medicalconsultation.diary.service.domain;

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
}
