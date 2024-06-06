package net.pladema.staff.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProfessionalDto {

    private Integer id;

    private String licenceNumber;

    private String firstName;

    private String lastName;

    private String identificationNumber;

    private String phoneNumber;

	private String nameSelfDetermination;

	private String middleNames;

	private String otherLastNames;

	private String fullName;

    public String getCompleteName(String name){
		return String.format("%s %s", name, lastName);
    }

	public ProfessionalDto(Integer id, String licenceNumber, String firstName, String lastName, String identificationNumber, String phoneNumber, String nameSelfDetermination) {
		this.id = id;
		this.licenceNumber = licenceNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.identificationNumber = identificationNumber;
		this.phoneNumber = phoneNumber;
		this.nameSelfDetermination = nameSelfDetermination;
	}
}
