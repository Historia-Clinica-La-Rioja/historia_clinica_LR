package net.pladema.person.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

import javax.annotation.Nullable;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BasicPersonalDataDto implements IBasicPersonalData {

    private String firstName;

	private String middleNames;

    private String lastName;

	private String otherLastNames;

    private String identificationNumber;

    private Short identificationTypeId;

	private String phonePrefix;

    private String phoneNumber;

    private Short genderId;

    private String nameSelfDetermination;

	@Nullable
	private String cuil;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	public BasicPersonalDataDto(String firstName, String lastName, String identificationNumber, Short identificationTypeId, String phonePrefix, String phoneNumber, Short genderId, String nameSelfDetermination) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.identificationNumber = identificationNumber;
		this.identificationTypeId = identificationTypeId;
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
		this.genderId = genderId;
		this.nameSelfDetermination = nameSelfDetermination;
	}

	public BasicPersonalDataDto(String firstName, String lastName, String identificationNumber, Short identificationTypeId, String phonePrefix, String phoneNumber, Short genderId, String nameSelfDetermination, LocalDate birthDate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.identificationNumber = identificationNumber;
		this.identificationTypeId = identificationTypeId;
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
		this.genderId = genderId;
		this.nameSelfDetermination = nameSelfDetermination;
		this.birthDate = birthDate;
	}
}
