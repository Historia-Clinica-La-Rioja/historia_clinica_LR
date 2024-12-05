package ar.lamansys.sgh.shared.infrastructure.output.rest.medicationrequestvalidation;

import lombok.Getter;

@Getter
public class MedicationRequestValidationException extends RuntimeException {

	private static final long serialVersionUID = 7966925907717494576L;

	private final EMedicationRequestValidationException code;

	public MedicationRequestValidationException(String message, EMedicationRequestValidationException code) {
		super(message);
		this.code = code;
	}

}
