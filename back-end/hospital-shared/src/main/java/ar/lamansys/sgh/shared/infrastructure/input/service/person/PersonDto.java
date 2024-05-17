package ar.lamansys.sgh.shared.infrastructure.input.service.person;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
public class PersonDto {
	private String firstName;
	private String middleNames;
	private String lastName;
	private String otherLastNames;
	private Short identificationTypeId;
	private String identificationTypeDescription;
	private String identificationNumber;
	private Short genderId;
	private String genderDescription;
	private LocalDate birthDate;
	private String cuil;
	private String selfDeterminationName;
	private Short selfDeterminationGender;
	private String selfDeterminationGenderDescription;
}
