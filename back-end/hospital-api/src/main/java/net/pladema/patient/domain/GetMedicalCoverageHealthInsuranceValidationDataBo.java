package net.pladema.patient.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMedicalCoverageHealthInsuranceValidationDataBo {

	private String medicalCoverageName;

	private String acronym;

	private String cuit;

	private String patientAffiliateNumber;

}
