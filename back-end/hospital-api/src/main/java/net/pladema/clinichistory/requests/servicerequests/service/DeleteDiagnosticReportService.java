package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.documents.repository.ips.entity.DiagnosticReport;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;

public interface DeleteDiagnosticReportService {
    DiagnosticReport execute(PatientInfoBo patient, Integer diagnosticReportId);
}
