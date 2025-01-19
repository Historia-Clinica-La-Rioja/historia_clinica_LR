package net.pladema.person.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.person.repository.entity.Person;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BackofficePersonDto {

	private Integer id;

	private String firstName;

	private String middleNames;

	private String lastName;

	private String otherLastNames;

	private Short identificationTypeId;

	private String identificationNumber;

	private Short genderId;

	private LocalDate birthDate;

	private String email;

	public BackofficePersonDto(Person person){
		this.id = person.getId();
		this.firstName = person.getFirstName();
		this.middleNames = person.getMiddleNames();
		this.lastName = person.getLastName();
		this.otherLastNames = person.getOtherLastNames();
		this.identificationTypeId = person.getIdentificationTypeId();
		this.identificationNumber = person.getIdentificationNumber();
		this.genderId = person.getGenderId();
		this.birthDate = person.getBirthDate();
	}

}
