package ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception;

import lombok.Getter;

@Getter
public class PrescriptionMedicationInvalidSoldUnitsException extends RuntimeException {

	private static final long serialVersionUID = 1828526820941975312L;

	public PrescriptionMedicationInvalidSoldUnitsException(String message) {
		super(message);
	}

}
