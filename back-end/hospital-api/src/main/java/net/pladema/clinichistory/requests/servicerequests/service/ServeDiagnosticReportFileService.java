package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.requests.servicerequests.service.domain.StoredFileBo;

public interface ServeDiagnosticReportFileService {
    StoredFileBo run(Integer fileId);
}
