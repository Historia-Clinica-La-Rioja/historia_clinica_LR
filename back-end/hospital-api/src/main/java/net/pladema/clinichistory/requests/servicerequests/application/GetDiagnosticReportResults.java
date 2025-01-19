package net.pladema.clinichistory.requests.servicerequests.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.DiagnosticReportStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.DiagnosticReportResultsBo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetDiagnosticReportResults {

    private final DiagnosticReportStorage diagnosticReportStorage;
    
    public DiagnosticReportResultsBo run(Integer diagnosticReportId) {
        log.debug("input -> diagnosticReportId {}", diagnosticReportId);
        var result = diagnosticReportStorage.getDiagnosticReportResults(diagnosticReportId);
        diagnosticReportStorage.getFilesByDiagnosticReport(diagnosticReportId)
                        .ifPresent(result::setFiles);

        log.debug("Output -> {}", result);
        return result;
    }
}
