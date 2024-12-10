package ar.lamansys.sgh.shared.domain.medicationrequestvalidation;

import ar.lamansys.sgh.shared.domain.ELicenseNumberTypeBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicationRequestValidationDispatcherProfessionalBo {

	private String name;

	private String lastName;

	private String identificationType;

	private String identificationNumber;

	private String licenseNumber;

	private String licenseType;

	private String email;

	public MedicationRequestValidationDispatcherProfessionalBo(String name, String lastName, String identificationType,
															   String identificationNumber, String licenseNumber, ELicenseNumberTypeBo licenseType,
															   String email) {
		this.name = name;
		this.lastName = lastName;
		this.identificationType = identificationType;
		this.identificationNumber = identificationNumber;
		this.licenseNumber = licenseNumber;
		this.licenseType = licenseType != null ? licenseType.getAcronym() : null;
		this.email = email;
	}
}
