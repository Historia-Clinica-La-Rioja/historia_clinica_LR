package net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions;

public class DiagnosticReportMissingObservationGroupException extends DiagnosticReportObservationException {
	public DiagnosticReportMissingObservationGroupException(Integer diagnosticReportId) {
		super("diagnostic-report-missing-observation-group", "diagnosticReportId", diagnosticReportId);
	}
}
