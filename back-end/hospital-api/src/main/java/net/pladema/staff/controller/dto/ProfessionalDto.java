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

    public String getCompleteName(String name){
		return String.format("%s %s", name, lastName);
    }
}
