package net.pladema.clinichistory.requests.servicerequests.service;

public interface DeleteDiagnosticReportService {
    Integer execute(Integer patientId, Integer diagnosticReportId);
}
