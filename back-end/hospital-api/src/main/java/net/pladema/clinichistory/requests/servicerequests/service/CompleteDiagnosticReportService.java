package net.pladema.clinichistory.requests.servicerequests.service;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.CompleteRequestDto;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;
import org.springframework.web.multipart.MultipartFile;

public interface CompleteDiagnosticReportService {
    void run(PatientInfoBo patient, Integer diagnosticReportId, CompleteDiagnosticReportBo completeDiagnosticReportBo, MultipartFile file);
}
