package ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception;

import lombok.Getter;

@Getter
public class PrescriptionMedicationIncorrectPaymentException extends RuntimeException {

	private static final long serialVersionUID = 1953872433458942763L;

	public PrescriptionMedicationIncorrectPaymentException(String message) {
		super(message);
	}

}
