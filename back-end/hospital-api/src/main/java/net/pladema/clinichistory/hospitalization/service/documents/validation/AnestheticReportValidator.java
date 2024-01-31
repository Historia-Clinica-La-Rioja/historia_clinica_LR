package net.pladema.clinichistory.hospitalization.service.documents.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.domain.AnestheticReportBo;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AnestheticReportValidator extends InternmentDocumentValidator {

    private final AnestheticSubstanceValidator anestheticSubstanceValidator;
    private final BloodRiskFactorsValidator bloodRiskFactorsValidator;

    public void assertContextValid(AnestheticReportBo anestheticReport) {

        super.assertContextValid(anestheticReport);
        super.assertDocumentValid(anestheticReport);
        this.assertMainDiagnosis(anestheticReport);
        super.assertProceduresValid(anestheticReport);
        super.assertAnthropometricData(anestheticReport);
        super.assertMedicationsValid(anestheticReport);
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getPreMedications());
        anestheticSubstanceValidator.assertSnomedAndDosageAndViaFields(anestheticReport.getAnestheticPlans());
        bloodRiskFactorsValidator.assertContextValid(anestheticReport.getRiskFactors());

        log.trace("Output -> isValid anestheticReport {}", anestheticReport);
    }

    private void assertMainDiagnosis(AnestheticReportBo anestheticReport) {
        if (anestheticReport.getMainDiagnosis() != null)
            super.assertDiagnosisValid(anestheticReport, anestheticReport.getMainDiagnosis());
    }
}
