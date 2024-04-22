package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.AnestheticStorage;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PatientNotFoundException;
import net.pladema.patient.controller.service.PatientExternalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenerateAnestheticReport {

    private final PatientExternalService patientExternalService;
    private final InternmentEpisodeService internmentEpisodeService;
    private final DiscardPreviousAnestheticReport discardPreviousAnestheticReport;
    private final CreateAnestheticReportDocument createAnestheticReportDocument;
    private final GetAnestheticReport getAnestheticReport;
    private final AnestheticStorage anestheticStorage;

    @Transactional
    public Integer run(AnestheticReportBo anestheticReport) {
        log.debug("Input parameter -> anestheticReport {}", anestheticReport);

        this.setPreviousDocumentId(anestheticReport);
        this.setPatientInfo(anestheticReport);

        if (anestheticReport.getPreviousDocumentId() != null)
            this.prepareNewAnestheticReport(anestheticReport);

        Integer id = createAnestheticReportDocument.run(anestheticReport);

        log.debug("Output -> anesthetic report id {} generated", id);
        return id;
    }

    private void setPatientInfo(AnestheticReportBo anestheticReport) {
        internmentEpisodeService.getPatient(anestheticReport.getEncounterId())
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresentOrElse(patientInfo -> {
                    anestheticReport.setPatientInfo(patientInfo);
                    anestheticReport.setPatientId(patientInfo.getId());
                }, PatientNotFoundException::new);
    }

    private void prepareNewAnestheticReport(AnestheticReportBo anestheticReport) {
        var previousAnestheticReport = getAnestheticReport.run(anestheticReport.getPreviousDocumentId(), anestheticReport.getEncounterId());
        setInitialDocument(anestheticReport, previousAnestheticReport);
        discardPreviousAnestheticReport.run(anestheticReport, previousAnestheticReport);
    }

    private void setInitialDocument(AnestheticReportBo anestheticReport, AnestheticReportBo previousAnestheticReport) {
        Long initialDocumentId = previousAnestheticReport.getInitialDocumentId();
        anestheticReport.setInitialDocumentId(initialDocumentId != null ? initialDocumentId : previousAnestheticReport.getId());
    }

    private void setPreviousDocumentId(AnestheticReportBo anestheticReport) {
        Integer internmentEpisodeId = anestheticReport.getEncounterId();
        Long lastDocumentId = anestheticStorage.getDocumentIdFromLastAnestheticReportDraft(internmentEpisodeId);
        anestheticReport.setPreviousDocumentId(lastDocumentId);
    }

}
