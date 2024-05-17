package ar.lamansys.sgh.publicapi.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PersonBo {
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
