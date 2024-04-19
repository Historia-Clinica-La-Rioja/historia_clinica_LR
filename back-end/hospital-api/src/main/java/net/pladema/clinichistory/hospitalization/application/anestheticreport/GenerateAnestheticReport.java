package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateAnestheticReport {

    private final DiscardPreviousAnestheticReport discardPreviousAnestheticReport;
    private final CreateAnestheticReportDocument createAnestheticReportDocument;
    private final GetAnestheticReport getAnestheticReport;

    @Transactional
    public Integer run(AnestheticReportBo anestheticReport) {
        log.debug("Input parameter -> anestheticReport {}", anestheticReport);

        if (anestheticReport.getPreviousDocumentId() != null)
            this.prepareNewAnestheticReport(anestheticReport);

        Integer id = createAnestheticReportDocument.run(anestheticReport);

        log.debug("Output -> anesthetic report id {} generated", id);
        return id;
    }

    private void prepareNewAnestheticReport(AnestheticReportBo anestheticReport) {
        var previousAnestheticReport = getAnestheticReport.run(anestheticReport.getPreviousDocumentId(), anestheticReport.getEncounterId());
        setInitialDocument(anestheticReport, previousAnestheticReport);
        discardPreviousAnestheticReport.run(anestheticReport, previousAnestheticReport);
    }

    private void setInitialDocument(AnestheticReportBo anestheticReport, AnestheticReportBo previousAnestheticReport) {
        Long initialDocumentId = previousAnestheticReport.getInitialDocumentId();
        anestheticReport.setInitialDocumentId(initialDocumentId != null ? initialDocumentId : anestheticReport.getId());
    }

}
