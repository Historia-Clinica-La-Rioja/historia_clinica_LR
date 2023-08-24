package ar.lamansys.sgh.publicapi.prescription.domain.exceptions;

public class PrescriptionRequestException extends RuntimeException {
	public PrescriptionRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
