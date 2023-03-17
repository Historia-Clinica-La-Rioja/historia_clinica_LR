package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.pladema.person.repository.domain.DuplicatePersonVo;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@ToString
public class DuplicatePatientDto {

	private String firstName;
	private String middleNames;
	private String lastName;
	private String otherLastNames;
	private Short identificationTypeId;
	private String identificationNumber;
	private LocalDate birthdate;
	private Long numberOfCandidates;

	public DuplicatePatientDto(DuplicatePersonVo duplicatePersonVo) {
		this.firstName = duplicatePersonVo.getFirstName();
		this.middleNames = duplicatePersonVo.getMiddleNames();
		this.lastName = duplicatePersonVo.getLastName();
		this.otherLastNames = duplicatePersonVo.getOtherLastNames();
		this.identificationTypeId = duplicatePersonVo.getIdentificationTypeId();
		this.identificationNumber = duplicatePersonVo.getIdentificationNumber();
		this.birthdate = duplicatePersonVo.getBirthdate();
		this.numberOfCandidates = duplicatePersonVo.getNumberOfCandidates();
	}

}
