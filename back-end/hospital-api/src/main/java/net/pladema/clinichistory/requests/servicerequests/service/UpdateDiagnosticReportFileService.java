package net.pladema.clinichistory.requests.servicerequests.service;

import java.util.List;

public interface UpdateDiagnosticReportFileService {
    void run(Integer diagnosticReportId, List<Integer> fileIds);
}
