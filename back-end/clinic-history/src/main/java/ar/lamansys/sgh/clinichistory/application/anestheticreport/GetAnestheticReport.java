package ar.lamansys.sgh.clinichistory.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.exceptions.AnestheticReportException;
import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.output.AnestheticReportStorage;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EDocumentStatus;
import ar.lamansys.sgh.clinichistory.domain.document.enums.EAnestheticReportException;
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
                .filter(anestheticReportBo -> !EDocumentStatus.isDeleted(anestheticReportBo))
                .map(fillOutAnestheticReport::run)
                .orElseThrow(() -> new AnestheticReportException(
                        EAnestheticReportException.ANESTHETIC_REPORT_NOT_FOUND,
                        "anesthetic-report.not-found"));

        log.debug("Output -> anestheticReport {}", result);
        return result;
    }
}
