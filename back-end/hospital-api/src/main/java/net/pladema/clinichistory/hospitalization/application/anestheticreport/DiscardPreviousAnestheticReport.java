package net.pladema.clinichistory.hospitalization.application.anestheticreport;

import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.services.ClinicalObservationService;
import ar.lamansys.sgh.clinichistory.domain.ips.services.HealthConditionService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ObservationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiscardPreviousAnestheticReport {

    private final SharedDocumentPort sharedDocumentPort;
    private final HealthConditionService healthConditionService;
    private final ClinicalObservationService clinicalObservationService;

    public void run(AnestheticReportBo anestheticReport, AnestheticReportBo previousAnestheticReport) {
        log.debug("Input parameter -> anestheticReport {}", anestheticReport);

        Long previousDocumentId = anestheticReport.getPreviousDocumentId();

        sharedDocumentPort.deleteDocument(anestheticReport.getPreviousDocumentId(), DocumentStatus.DRAFT_DISCARDED);

        this.setNullAllIds(anestheticReport, previousAnestheticReport);

        log.debug("Output -> discarded anesthetic report previous document id {}", previousDocumentId);
    }

    private void setNullAllIds(AnestheticReportBo anestheticReport, AnestheticReportBo previousAnestheticReport) {

        Optional.ofNullable(anestheticReport.getMainDiagnosis()).ifPresent(c -> c.setId(null));

        Optional.ofNullable(previousAnestheticReport.getMainDiagnosis())
                .ifPresent(previousMainDiagnosis -> {
                    previousMainDiagnosis.setId(null);
                    previousMainDiagnosis.setStatusId(ConditionClinicalStatus.DRAFT_DISCARDED);
                    previousMainDiagnosis.setVerificationId(ConditionVerificationStatus.DISCARDED);
                    healthConditionService.loadMainDiagnosis(anestheticReport.getPatientInfo(), anestheticReport.getPreviousDocumentId(), Optional.of(previousMainDiagnosis));
                });

        Optional.ofNullable(anestheticReport.getSurgeryProcedures())
                .ifPresent(l -> l.addAll(getDischargedConcepts(anestheticReport.getSurgeryProcedures(), previousAnestheticReport.getSurgeryProcedures(), ProceduresStatus.DRAFT_DISCARDED)));

        Optional.ofNullable(anestheticReport.getAnthropometricData())
                .ifPresent(anthropometricDataBo -> {
                    Optional.ofNullable(anthropometricDataBo.getBloodType())
                            .ifPresent(c -> c.setId(null));
                    Optional.ofNullable(anthropometricDataBo.getHeight())
                            .ifPresent(c -> c.setId(null));
                    Optional.ofNullable(anthropometricDataBo.getWeight())
                            .ifPresent(c -> c.setId(null));
                });

        Optional.ofNullable(previousAnestheticReport.getAnthropometricData())
                .ifPresent(previousAnthropometricData -> {
                    var previousBloodType = previousAnthropometricData.getBloodType();
                    if (previousBloodType != null) {
                        previousBloodType.setId(null);
                        previousBloodType.setStatusId(ObservationStatus.DRAFT_DISCARDED);
                    }
                    var previousHeight = previousAnthropometricData.getHeight();
                    if (previousHeight != null) {
                        previousHeight.setId(null);
                        previousHeight.setStatusId(ObservationStatus.DRAFT_DISCARDED);
                    }
                    var previousWeight = previousAnthropometricData.getWeight();
                    if (previousWeight != null) {
                        previousWeight.setId(null);
                        previousWeight.setStatusId(ObservationStatus.DRAFT_DISCARDED);
                    }
                    clinicalObservationService.loadAnthropometricData(anestheticReport.getPatientId(), anestheticReport.getPreviousDocumentId(), Optional.of(previousAnthropometricData));
                });

        Optional.ofNullable(anestheticReport.getRiskFactors())
                .ifPresent(riskFactorBo -> {
                    Optional.ofNullable(riskFactorBo.getDiastolicBloodPressure())
                            .ifPresent(c -> c.setId(null));
                    Optional.ofNullable(riskFactorBo.getSystolicBloodPressure())
                            .ifPresent(c -> c.setId(null));
                    Optional.ofNullable(riskFactorBo.getHematocrit())
                            .ifPresent(c -> c.setId(null));
                });

        Optional.ofNullable(anestheticReport.getMedications())
                .ifPresent(l -> l.addAll(getDischargedConcepts(anestheticReport.getMedications(), previousAnestheticReport.getMedications(), MedicationStatementStatus.DRAFT_DISCARDED)));

        Optional.ofNullable(anestheticReport.getPreMedications()).ifPresent(l -> l.forEach(e -> e.setId(null)));

        Optional.ofNullable(anestheticReport.getHistories())
                .ifPresent(l -> l.addAll(getDischargedConcepts(anestheticReport.getHistories(), previousAnestheticReport.getHistories(), ConditionClinicalStatus.DRAFT_DISCARDED)
                        .stream()
                        .peek(a -> a.setVerificationId(ConditionVerificationStatus.DISCARDED))
                        .collect(Collectors.toList()))
                );

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

    private <T extends ClinicalTerm> List<T> getDischargedConcepts(List<T> newTerms, List<T> oldTerms, String errorCode) {
        if (newTerms.isEmpty() || oldTerms.isEmpty())
            return new ArrayList<>();
        return oldTerms.stream()
                .filter(term -> newTerms.stream()
                        .noneMatch(term::equals))
                .peek(p -> {
                    p.setId(null);
                    p.setStatusId(errorCode);
                })
                .collect(Collectors.toList());
    }
}
