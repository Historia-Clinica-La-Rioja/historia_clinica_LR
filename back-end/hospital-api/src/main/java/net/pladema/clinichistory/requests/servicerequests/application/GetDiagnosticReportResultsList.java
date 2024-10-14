package net.pladema.clinichistory.requests.servicerequests.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.port.DiagnosticReportStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.DiagnosticReportResultsBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetDiagnosticReportResultsList {

    private final DiagnosticReportStorage diagnosticReportStorage;

    public List<DiagnosticReportResultsBo> run(DiagnosticReportFilterBo filter) {
        log.debug("Input parameters -> diagnosticReportFilterBo {}", filter);
        List<DiagnosticReportResultsBo> result = diagnosticReportStorage.getList(filter);
        log.debug("Output size -> {}", result.size());
        log.trace("Output -> {}", result);
        return result;
    }
}
