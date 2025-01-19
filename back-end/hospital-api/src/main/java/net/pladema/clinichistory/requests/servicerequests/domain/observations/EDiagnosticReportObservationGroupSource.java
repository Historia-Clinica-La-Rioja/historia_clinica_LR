package net.pladema.clinichistory.requests.servicerequests.domain.observations;

public enum EDiagnosticReportObservationGroupSource {
	/* Observations created via the web app form */
	WITH_PROCEDURE_TEMPLATE,
	/* Observations uploaded via the fhir api*/
	WITHOUT_PROCEDURE_TEMPLATE,
	/* There's no observation group found*/
	NOT_FOUND
}
