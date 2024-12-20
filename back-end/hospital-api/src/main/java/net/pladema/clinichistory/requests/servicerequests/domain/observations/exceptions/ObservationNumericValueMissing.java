package net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions;

public class ObservationNumericValueMissing extends DiagnosticReportObservationException {

	public ObservationNumericValueMissing(Integer observationId) {
		super("observation-numeric-value-missing", "observationId", observationId);
	}
}
