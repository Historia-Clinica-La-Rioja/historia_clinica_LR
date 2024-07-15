package ar.lamansys.sgh.publicapi.patient.application.saveexternalpatient.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class SaveExternalPatientAccessDeniedException extends PublicApiAccessDeniedException {

	public SaveExternalPatientAccessDeniedException() {
		super("Patient","SaveExternalPatient");
	}
}
