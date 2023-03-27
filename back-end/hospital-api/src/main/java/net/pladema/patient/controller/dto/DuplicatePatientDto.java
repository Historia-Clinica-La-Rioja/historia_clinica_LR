package net.pladema.patient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.pladema.person.repository.domain.DuplicatePersonVo;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
public class DuplicatePatientDto {

	private String firstName;
	private String middleNames;
	private String lastName;
	private String otherLastNames;
	private Short identificationTypeId;
	private String identificationNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
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
