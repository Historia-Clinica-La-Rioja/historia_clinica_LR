package net.pladema.emergencycare.repository.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfessionalPersonVo {

	private Integer id;

	private String firstName;

	private String lastName;

	private String nameSelfDetermination;

	private String middleNames;

	private String otherLastNames;

	public ProfessionalPersonVo(String firstName, String lastName, String nameSelfDetermination, String middleNames, String otherLastNames) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.nameSelfDetermination = nameSelfDetermination;
		this.middleNames = middleNames;
		this.otherLastNames = otherLastNames;
	}
}
