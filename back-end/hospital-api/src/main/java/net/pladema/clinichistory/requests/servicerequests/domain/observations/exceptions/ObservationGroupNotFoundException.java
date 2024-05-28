package net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions;

public class ObservationGroupNotFoundException extends DiagnosticReportObservationException {

	public ObservationGroupNotFoundException(Integer observationGroupId) {
		super("observation-group-not-found", "observationGroupId", observationGroupId);
	}
}
