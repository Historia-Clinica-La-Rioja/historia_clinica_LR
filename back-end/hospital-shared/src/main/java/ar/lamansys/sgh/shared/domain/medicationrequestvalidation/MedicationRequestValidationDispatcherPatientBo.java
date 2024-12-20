package ar.lamansys.sgh.shared.domain.medicationrequestvalidation;

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
public class MedicationRequestValidationDispatcherPatientBo {

	private String name;

	private String lastName;

	private String identificationType;

	private String identificationNumber;

	private MedicationRequestValidationDispatcherMedicalCoverageBo medicalCoverage;

	public MedicationRequestValidationDispatcherPatientBo(String name, String lastName, String identificationType, String identificationNumber) {
		this.name = name;
		this.lastName = lastName;
		this.identificationType = identificationType;
		this.identificationNumber = identificationNumber;
	}

}
