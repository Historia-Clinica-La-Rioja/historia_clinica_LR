package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import java.util.List;

public interface DiagnosticReportService {
    List<DiagnosticReportBo> loadDiagnosticReport(Long documentId, Integer patientId, List<DiagnosticReportBo> studyBos);
}
