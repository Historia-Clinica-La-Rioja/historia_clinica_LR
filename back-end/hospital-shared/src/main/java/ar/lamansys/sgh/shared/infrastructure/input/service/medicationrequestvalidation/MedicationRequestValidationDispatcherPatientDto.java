package ar.lamansys.sgh.shared.infrastructure.input.service.medicationrequestvalidation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicationRequestValidationDispatcherPatientDto {

	private String name;

	private String lastName;

	private String identificationType;

	private String identificationNumber;

	private MedicationRequestValidationDispatcherMedicalCoverageDto medicalCoverage;

}
