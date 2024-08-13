package ar.lamansys.sgh.publicapi.patient.application.fetchpatientpersonbyid.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class PatientPersonAccessDeniedException extends PublicApiAccessDeniedException {

	public PatientPersonAccessDeniedException() {
		super("PatientInformation", "FetchPatientPersonById");
	}
}
