package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;

public interface CompleteDiagnosticReportService {
    Integer run(PatientInfoBo patient, Integer diagnosticReportId, CompleteDiagnosticReportBo completeDiagnosticReportBo);
}
