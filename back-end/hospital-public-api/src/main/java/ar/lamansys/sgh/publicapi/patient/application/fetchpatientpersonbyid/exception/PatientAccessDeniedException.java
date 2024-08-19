package ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class PatientAccessDeniedException extends PublicApiAccessDeniedException {

	public PatientAccessDeniedException() {
		super("PatientInformation", "FetchPatientPersonById");
	}
}
