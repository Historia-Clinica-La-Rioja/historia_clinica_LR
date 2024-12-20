package ar.lamansys.sgh.clinichistory.application.anestheticreport.validations;

import ar.lamansys.sgh.clinichistory.application.document.validators.BloodRiskFactorsValidator;
import ar.lamansys.sgh.clinichistory.application.document.validators.GeneralDocumentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AnestheticReportValidator {

    private final GeneralDocumentValidator generalDocumentValidator;
    private final BloodRiskFactorsValidator bloodRiskFactorsValidator;
    private final AnestheticSubstanceValidator anestheticSubstanceValidator;
    private final AnestheticTechniqueValidator anestheticTechniqueValidator;
    private final ProcedureDescriptionValidator procedureDescriptionValidator;
    private final MeasuringPointValidator measuringPointValidator;

    public void assertContextValid(AnestheticReportBo anestheticReport) {

        generalDocumentValidator.assertContextValid(anestheticReport);
        generalDocumentValidator.assertDocumentValid(anestheticReport);
        this.assertMainDiagnosis(anestheticReport);
        generalDocumentValidator.assertProceduresValid(anestheticReport);
        generalDocumentValidator.assertAnthropometricData(anestheticReport);
        bloodRiskFactorsValidator.assertContextValid(anestheticReport.getRiskFactors());
        generalDocumentValidator.assertMedicationsValid(anestheticReport);
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getPreMedications());
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getAnestheticPlans());
        anestheticSubstanceValidator.assertSnomedAndDosageFields(anestheticReport.getAnalgesicTechniques());
        anestheticTechniqueValidator.assertContextValid(anestheticReport.getAnestheticTechniques());
        anestheticSubstanceValidator.assertSnomed(anestheticReport.getFluidAdministrations());
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getAnestheticAgents());
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getNonAnestheticDrugs());
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getAntibioticProphylaxis());
        measuringPointValidator.assertContextValid(anestheticReport.getMeasuringPoints());
        procedureDescriptionValidator.assertStartEndDatesTimes(anestheticReport.getProcedureDescription());

        log.trace("Output -> isValid anestheticReport {}", anestheticReport);
    }

    private void assertMainDiagnosis(AnestheticReportBo anestheticReport) {
        if (anestheticReport.getMainDiagnosis() != null)
            generalDocumentValidator.assertDiagnosisValid(anestheticReport, anestheticReport.getMainDiagnosis());
    }
}
