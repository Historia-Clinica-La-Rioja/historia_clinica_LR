package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;

public interface DeleteDiagnosticReportService {
    Integer execute(PatientInfoBo patient, Integer diagnosticReportId);
}
