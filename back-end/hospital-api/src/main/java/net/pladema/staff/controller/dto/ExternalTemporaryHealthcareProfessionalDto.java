package net.pladema.staff.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExternalTemporaryHealthcareProfessionalDto {

	private String firstName;
	private String lastName;
	private Short identificationTypeId;
	private String identificationNumber;
	private String licenseNumber;
}
