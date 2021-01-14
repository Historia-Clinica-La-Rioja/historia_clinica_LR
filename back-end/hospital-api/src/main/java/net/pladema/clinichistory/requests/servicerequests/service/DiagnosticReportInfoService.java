package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;

public interface DiagnosticReportInfoService {
    DiagnosticReportBo run(Integer diagnosticReportId);
}
