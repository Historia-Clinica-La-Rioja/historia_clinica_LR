package ar.lamansys.sgh.publicapi.appointment.application.fetchmedicalcoverages.exception;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class FetchMedicalCoveragesAccessDeniedException extends PublicApiAccessDeniedException {

	public FetchMedicalCoveragesAccessDeniedException() {
		super("Appointment", "FetchMedicalCoverages");
	}
}
