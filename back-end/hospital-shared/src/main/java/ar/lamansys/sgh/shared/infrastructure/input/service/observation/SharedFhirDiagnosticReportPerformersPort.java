package ar.lamansys.sgh.shared.infrastructure.input.service.observation;

public interface SharedFhirDiagnosticReportPerformersPort {
	void savePerformers(Integer diagnosticReportId, FhirDiagnosticReportPerformersDto performersDto);
}
