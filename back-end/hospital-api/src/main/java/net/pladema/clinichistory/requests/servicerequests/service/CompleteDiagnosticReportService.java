package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;

public interface CompleteDiagnosticReportService {
    Integer run(PatientInfoBo patient, Integer diagnosticReportId, CompleteDiagnosticReportBo completeDiagnosticReportBo);
}
