package ar.lamansys.sgh.publicapi.patient.application.saveexternalpatient.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class SaveExternalPatientAccessDeniedException extends PublicApiAccessDeniedException {

	public SaveExternalPatientAccessDeniedException() {
		super("Patient","SaveExternalPatient");
	}
}
