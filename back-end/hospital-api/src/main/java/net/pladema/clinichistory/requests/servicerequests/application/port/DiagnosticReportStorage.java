package net.pladema.clinichistory.requests.servicerequests.application.port;

import ar.lamansys.sgh.clinichistory.domain.ips.FileBo;
import net.pladema.clinichistory.requests.servicerequests.domain.DiagnosticReportResultsBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;

import java.util.List;
import java.util.Optional;

public interface DiagnosticReportStorage {

    DiagnosticReportResultsBo getDiagnosticReportResults(Integer diagnosticReportId);
    Optional<List<FileBo>> getFilesByDiagnosticReport(Integer diagnosticReportId);
    List<DiagnosticReportResultsBo> getList(DiagnosticReportFilterBo filterBo);
}
