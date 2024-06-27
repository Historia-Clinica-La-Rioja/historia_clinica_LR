package ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception;

import lombok.Getter;

@Getter
public class PrescriptionMedicationBlankFieldException extends RuntimeException {

	private static final long serialVersionUID = -7014408915404702003L;

	public PrescriptionMedicationBlankFieldException(String message) {
		super(message);
	}

}
