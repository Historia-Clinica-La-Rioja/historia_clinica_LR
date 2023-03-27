package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.person.repository.domain.PersonSearchResultVo;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PatientPersonalInfoDto {

	private Integer patientId;
	private String firstName;
	private String middleNames;
	private String lastName;
	private String otherLastNames;
	private Short identificationTypeId;
	private String identificationNumber;
	private LocalDate birthdate;
	private Short genderId;
	private String phonePrefix;
	private String phoneNumber;
	private String nameSelfDetermination;
	private Short typeId;

	public PatientPersonalInfoDto(PersonSearchResultVo personSearchResultVo) {
		this.patientId = personSearchResultVo.getPatientId();
		this.firstName = personSearchResultVo.getFirstName();
		this.middleNames = personSearchResultVo.getMiddleNames();
		this.lastName = personSearchResultVo.getLastName();
		this.otherLastNames = personSearchResultVo.getOtherLastNames();
		this.identificationTypeId = personSearchResultVo.getIdentificationTypeId();
		this.identificationNumber = personSearchResultVo.getIdentificationNumber();
		this.birthdate = personSearchResultVo.getBirthdate();
		this.genderId = personSearchResultVo.getGenderId();
		this.phonePrefix = personSearchResultVo.getPhonePrefix();
		this.phoneNumber = personSearchResultVo.getPhoneNumber();
		this.nameSelfDetermination = personSearchResultVo.getNameSelfDetermination();
		this.typeId = personSearchResultVo.getTypeId();
	}

}
