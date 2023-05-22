package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PatientBo {

	private Integer id;

	private String firstName;

	private String middleNames;

	private String lastName;

	private String otherLastNames;

	private Short identificationType;

	private String identificationNumber;

	private LocalDate birthDate;

	private Short genderId;

}
