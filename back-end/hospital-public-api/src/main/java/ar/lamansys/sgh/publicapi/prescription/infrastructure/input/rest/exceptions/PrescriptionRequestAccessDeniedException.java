package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class PrescriptionRequestAccessDeniedException extends PublicApiAccessDeniedException {
	public PrescriptionRequestAccessDeniedException() {
		super("Prescription", "Request");
	}
}
