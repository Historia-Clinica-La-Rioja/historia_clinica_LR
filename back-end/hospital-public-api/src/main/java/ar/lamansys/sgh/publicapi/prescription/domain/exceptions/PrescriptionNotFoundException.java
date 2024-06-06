package ar.lamansys.sgh.publicapi.prescription.domain.exceptions;

public class PrescriptionNotFoundException extends RuntimeException {
	public PrescriptionNotFoundException(String message) {
		super(message);
	}
}
