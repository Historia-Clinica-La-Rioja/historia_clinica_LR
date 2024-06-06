package net.pladema.clinichistory.hospitalization.service.documents.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AnestheticReportValidator extends InternmentDocumentValidator {

    private final BloodRiskFactorsValidator bloodRiskFactorsValidator;
    private final AnestheticSubstanceValidator anestheticSubstanceValidator;
    private final AnestheticTechniqueValidator anestheticTechniqueValidator;
    private final ProcedureDescriptionValidator procedureDescriptionValidator;
    private final MeasuringPointValidator measuringPointValidator;

    public void assertContextValid(AnestheticReportBo anestheticReport) {

        super.assertContextValid(anestheticReport);
        super.assertDocumentValid(anestheticReport);
        this.assertMainDiagnosis(anestheticReport);
        super.assertProceduresValid(anestheticReport);
        super.assertAnthropometricData(anestheticReport);
        bloodRiskFactorsValidator.assertContextValid(anestheticReport.getRiskFactors());
        super.assertMedicationsValid(anestheticReport);
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getPreMedications());
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getAnestheticPlans());
        anestheticSubstanceValidator.assertSnomedAndDosageFields(anestheticReport.getAnalgesicTechniques());
        anestheticTechniqueValidator.assertContextValid(anestheticReport.getAnestheticTechniques());
        anestheticSubstanceValidator.assertSnomed(anestheticReport.getFluidAdministrations());
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getAnestheticAgents());
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getNonAnestheticDrugs());
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getAntibioticProphylaxis());
        measuringPointValidator.assertContextValid(anestheticReport.getMeasuringPoints());

        log.trace("Output -> isValid anestheticReport {}", anestheticReport);
    }

    private void assertMainDiagnosis(AnestheticReportBo anestheticReport) {
        if (anestheticReport.getMainDiagnosis() != null)
            super.assertDiagnosisValid(anestheticReport, anestheticReport.getMainDiagnosis());
    }
}
