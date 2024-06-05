package ar.lamansys.sgh.clinichistory.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.exceptions.AnestheticReportException;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.AnestheticReportStorage;
import ar.lamansys.sgh.clinichistory.domain.document.exceptions.AnestheticReportEnumException;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetAnestheticReport {

    private final AnestheticReportStorage anestheticReportStorage;
    private final FillOutAnestheticReport fillOutAnestheticReport;

    public AnestheticReportBo run(Long documentId) {
        log.debug("Input parameters -> documentId {}", documentId);

        var result = anestheticReportStorage.get(documentId)
                .map(fillOutAnestheticReport::run)
                .orElseThrow(() -> new AnestheticReportException(
                        AnestheticReportEnumException.ANESTHETIC_REPORT_NOT_FOUND,
                        "anesthetic-report.not-found"));

        log.debug("Output -> anestheticReport {}", result);
        return result;
    }
}
