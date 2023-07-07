package net.pladema.edMonton.get.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EdMontonSummary {

	private String tittle;

	private String nameProfessional;

	private String middleNameProfessional;

	private String lastNameProfessional;

	private String licenseNumber;

	private String createdOn;

	public EdMontonSummary(String name, String middleNames, String lastName, String licenseNumber, String date){
		this.nameProfessional = name;
		this.middleNameProfessional = middleNames;
		this.lastNameProfessional = lastName;
		this.licenseNumber = licenseNumber;
		this.createdOn = date;
	}
}
