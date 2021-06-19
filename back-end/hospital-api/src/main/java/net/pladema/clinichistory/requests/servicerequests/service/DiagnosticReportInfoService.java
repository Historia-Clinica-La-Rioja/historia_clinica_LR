package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;

public interface DiagnosticReportInfoService {
    DiagnosticReportBo run(Integer diagnosticReportId);
}
