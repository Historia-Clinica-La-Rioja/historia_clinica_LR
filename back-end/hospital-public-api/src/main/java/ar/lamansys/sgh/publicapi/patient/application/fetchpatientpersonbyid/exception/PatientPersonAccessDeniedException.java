package ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class PatientPersonAccessDeniedException extends PublicApiAccessDeniedException {

	public PatientPersonAccessDeniedException() {
		super("PatientInformation", "FetchPatientPersonById");
	}
}
