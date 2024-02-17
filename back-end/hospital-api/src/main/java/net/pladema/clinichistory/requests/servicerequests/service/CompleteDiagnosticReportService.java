package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;

public interface CompleteDiagnosticReportService {
    Integer run(Integer patientId, Integer diagnosticReportId, CompleteDiagnosticReportBo completeDiagnosticReportBo, Integer institutionId);
}
