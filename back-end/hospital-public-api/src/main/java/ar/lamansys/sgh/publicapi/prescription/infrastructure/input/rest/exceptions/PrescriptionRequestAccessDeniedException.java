package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class PrescriptionRequestAccessDeniedException extends PublicApiAccessDeniedException {
	public PrescriptionRequestAccessDeniedException() {
		super("Prescription", "Request");
	}
}
