package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiscardPreviousAnestheticReport {

    private final SharedDocumentPort sharedDocumentPort;

    public void run(AnestheticReportBo anestheticReport, AnestheticReportBo previousAnestheticReport) {
        log.debug("Input parameter -> anestheticReport {}", anestheticReport);

        Long previousDocumentId = anestheticReport.getPreviousDocumentId();

        sharedDocumentPort.deleteDocument(anestheticReport.getPreviousDocumentId(), DocumentStatus.DRAFT_DISCARDED);

        this.setNullAllIds(anestheticReport);

        log.debug("Output -> discarded anesthetic report previous document id {}", previousDocumentId);
    }

    private void setNullAllIds(AnestheticReportBo anestheticReport) {

        Optional.ofNullable(anestheticReport.getMainDiagnosis()).ifPresent(c -> c.setId(null));

        Optional.ofNullable(anestheticReport.getSurgeryProcedures()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getAnthropometricData()).ifPresent(anthropometricDataBo -> {
            Optional.ofNullable(anthropometricDataBo.getBloodType()).ifPresent(c -> c.setId(null));
            Optional.ofNullable(anthropometricDataBo.getHeight()).ifPresent(c -> c.setId(null));
            Optional.ofNullable(anthropometricDataBo.getWeight()).ifPresent(c -> c.setId(null));
        });

        Optional.ofNullable(anestheticReport.getRiskFactors()).ifPresent(riskFactorBo -> {
            Optional.ofNullable(riskFactorBo.getDiastolicBloodPressure()).ifPresent(c -> c.setId(null));
            Optional.ofNullable(riskFactorBo.getSystolicBloodPressure()).ifPresent(c -> c.setId(null));
            Optional.ofNullable(riskFactorBo.getHematocrit()).ifPresent(c -> c.setId(null));
        });

        Optional.ofNullable(anestheticReport.getMedications()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getPreMedications()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getHistories()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getProcedureDescription()).ifPresent(c -> c.setId(null));

        Optional.ofNullable(anestheticReport.getAnestheticPlans()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getAnalgesicTechniques()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getAnestheticTechniques()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getFluidAdministrations()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getAnestheticAgents()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getNonAnestheticDrugs()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getAntibioticProphylaxis()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getMeasuringPoints()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getPostAnesthesiaStatus()).ifPresent(c -> c.setId(null));

    }
}
