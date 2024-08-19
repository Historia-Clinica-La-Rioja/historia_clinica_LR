package net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions;

public class DiagnosticReportNotFoundException extends DiagnosticReportObservationException {
	public DiagnosticReportNotFoundException(Integer diagnosticReportId) {
		super("diagnostic-report-not-found", "diagnosticReportId", diagnosticReportId);
	}
}
