package ar.lamansys.sgh.publicapi.appointment.application.fetchmedicalcoverages.exception;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class FetchMedicalCoveragesAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchMedicalCoveragesAccessDeniedException() {
		super("Appointment", "FetchMedicalCoverages");
	}
}
