package net.pladema.cipres.domain;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Builder
@Getter
public class BasicDataPersonBo {

	private String firstName;

	private String middleNames;

	private String lastName;

	private String otherLastNames;

	private Short identificationTypeId;

	private String identificationNumber;

	private LocalDate birthDate;

	private Short genderId;

}