package net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions;

public class ObservationNotFoundException extends DiagnosticReportObservationException {

	public ObservationNotFoundException(Integer observationId) {
		super("observation-not-found", "observationId", observationId);
	}

}
