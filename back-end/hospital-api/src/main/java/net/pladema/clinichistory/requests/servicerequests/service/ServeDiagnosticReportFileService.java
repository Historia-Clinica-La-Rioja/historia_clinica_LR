package net.pladema.clinichistory.requests.servicerequests.service;

import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;

public interface ServeDiagnosticReportFileService {
    StoredFileBo run(Integer fileId);
}
