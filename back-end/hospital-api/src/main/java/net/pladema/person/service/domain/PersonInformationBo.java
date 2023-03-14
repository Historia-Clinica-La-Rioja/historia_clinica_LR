package net.pladema.person.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PersonInformationBo {

	private String firstName;

	private String middleNames;

	private String lastName;

	private String otherLastNames;

	private String identificationNumber;

	private String identificationTypeDescription;

	private LocalDate birthDate;

	private Short patientTypeId;

}
