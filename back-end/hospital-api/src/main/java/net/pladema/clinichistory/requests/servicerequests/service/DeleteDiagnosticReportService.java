package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;

public interface DeleteDiagnosticReportService {
    Integer execute(PatientInfoBo patient, Integer diagnosticReportId);
}
