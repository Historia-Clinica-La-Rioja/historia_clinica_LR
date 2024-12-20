package ar.lamansys.sgh.shared.infrastructure.input.service;

public interface SharedDiagnosticReportPort {

	void completeDiagnosticReport(Integer serviceRequestId, Integer patientId, String notes);

}