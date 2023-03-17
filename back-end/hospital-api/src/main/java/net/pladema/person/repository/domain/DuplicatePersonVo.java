package net.pladema.person.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DuplicatePersonVo {

	private String firstName;
	private String middleNames;
	private String lastName;
	private String otherLastNames;
	private Short identificationTypeId;
	private String identificationNumber;
	private LocalDate birthdate;
	private Long numberOfCandidates;

	public DuplicatePersonVo(String firstName, String middleNames, String lastName, String otherLastNames, Short identificationTypeId, String identificationNumber, Long numberOfCandidates) {
		this.firstName = firstName;
		this.middleNames = middleNames;
		this.lastName = lastName;
		this.otherLastNames = otherLastNames;
		this.identificationTypeId = identificationTypeId;
		this.identificationNumber = identificationNumber;
		this.birthdate = null;
		this.numberOfCandidates = numberOfCandidates;
	}

	public DuplicatePersonVo(String firstName, String middleNames, String lastName, String otherLastNames, LocalDate birthdate, Long numberOfCandidates) {
		this.firstName = firstName;
		this.middleNames = middleNames;
		this.lastName = lastName;
		this.otherLastNames = otherLastNames;
		this.identificationTypeId = null;
		this.identificationNumber = null;
		this.birthdate = birthdate;
		this.numberOfCandidates = numberOfCandidates;
	}

	public DuplicatePersonVo(Short identificationTypeId, String identificationNumber, Long numberOfCandidates) {
		this.firstName = null;
		this.middleNames = null;
		this.lastName = null;
		this.otherLastNames = null;
		this.identificationTypeId = identificationTypeId;
		this.identificationNumber = identificationNumber;
		this.birthdate = null;
		this.numberOfCandidates = numberOfCandidates;
	}
}
