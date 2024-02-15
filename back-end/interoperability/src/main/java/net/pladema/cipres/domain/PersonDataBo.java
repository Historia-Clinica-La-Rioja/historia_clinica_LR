package net.pladema.cipres.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
public class PersonDataBo {

	private String firstName;

	private String middleNames;

	private String lastName;

	private String otherLastNames;

	private Short identificationTypeId;

	private String identificationNumber;

	private Short genderId;

	private LocalDate birthDate;

	private String phonePrefix;

	private String phoneNumber;

	private String email;

	private String street;

	private String number;

	private String floor;

	private String apartment;

	private String quarter;

	private String postcode;

	private String department;

	private String city;

	private String cityBahraCode;

	private String country;

}
