package net.pladema.staff.service.domain;

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
public class BasicPersonalDataBo {

    private String firstName;

    private String lastName;

    private String identificationNumber;

	private String nameSelfDetermination;

	private String middleNames;

	private String otherLastNames;

	private String fullName;

	public BasicPersonalDataBo(String firstName, String lastName, String identificationNumber, String nameSelfDetermination) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.identificationNumber = identificationNumber;
		this.nameSelfDetermination = nameSelfDetermination;
	}

	public BasicPersonalDataBo(String firstName, String lastName, String identificationNumber,
							   String nameSelfDetermination, String middleNames, String otherLastNames) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.identificationNumber = identificationNumber;
		this.nameSelfDetermination = nameSelfDetermination;
		this.middleNames = middleNames;
		this.otherLastNames = otherLastNames;
	}
}
