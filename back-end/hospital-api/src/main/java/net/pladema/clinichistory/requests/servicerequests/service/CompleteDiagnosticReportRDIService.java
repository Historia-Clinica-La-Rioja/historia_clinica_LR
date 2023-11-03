package net.pladema.clinichistory.requests.servicerequests.service;

public interface CompleteDiagnosticReportRDIService {
    Integer run(Integer patientId, Integer diagnosticReportId);
}
