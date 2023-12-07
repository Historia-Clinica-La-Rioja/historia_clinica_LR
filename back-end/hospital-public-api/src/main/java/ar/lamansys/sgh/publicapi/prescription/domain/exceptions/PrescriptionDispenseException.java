package ar.lamansys.sgh.publicapi.prescription.domain.exceptions;

public class PrescriptionDispenseException extends RuntimeException {
	public PrescriptionDispenseException(String message, Throwable cause) {
		super(message, cause);
	}
}
