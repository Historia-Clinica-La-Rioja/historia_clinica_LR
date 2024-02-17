package net.pladema.staff.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExternalTemporaryHealthcareProfessionalBo {

	private String firstName;
	private String lastName;
	private Short identificationTypeId;
	private String identificationNumber;
	private String licenseNumber;
}
