package ar.lamansys.sgh.publicapi.prescription.domain.exceptions;

public class PrescriptionIdMatchException extends RuntimeException {
	public PrescriptionIdMatchException(String message) {
		super(message);
	}
}
