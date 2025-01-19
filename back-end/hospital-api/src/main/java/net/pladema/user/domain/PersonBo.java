package net.pladema.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonBo {
	private Integer patientId;
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
