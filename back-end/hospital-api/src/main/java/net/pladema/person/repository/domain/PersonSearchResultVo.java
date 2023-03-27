package net.pladema.person.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PersonSearchResultVo {

	private Integer patientId;
	private String firstName;
	private String middleNames;
	private String lastName;
	private String otherLastNames;
	private Short identificationTypeId;
	private String identificationNumber;
	private LocalDate birthdate;
	private Short genderId;
	private String phonePrefix;
	private String phoneNumber;
	private String nameSelfDetermination;
	private Short typeId;

}
