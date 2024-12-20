package ar.lamansys.sgh.shared.infrastructure.output.rest.medicationrequestvalidation;

import lombok.Getter;

@Getter
public enum EMedicationRequestValidationException {

	NO_COVERAGE_AFFILIATE_NUMBER,
	COULD_NOT_VALIDATE_MEDICATION,
	MEDICAL_COVERAGE_NOT_SUPPORTED,
	EXTERNAL_ERROR;

}
