package ar.lamansys.sgh.clinichistory.application.document.validators;

import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import java.util.Collections;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BloodRiskFactorsValidator {

    private static final Integer MIN_BLOOD = 0;
    private static final Integer MAX_BLOOD = 1000;
    private static final Double MIN_PERCENTAGE = 0.0;
    private static final Double MAX_PERCENTAGE = 100.0;

    public void assertContextValid(RiskFactorBo riskFactorBo) {

        if (riskFactorBo == null || riskFactorBo.isEmpty())
            return;

        var diastolicBloodPressure = riskFactorBo.getDiastolicBloodPressure();
        this.assertBloodPressure(diastolicBloodPressure);

        var systolicBloodPressure = riskFactorBo.getSystolicBloodPressure();
        this.assertBloodPressure(systolicBloodPressure);

        var hematocrit = riskFactorBo.getHematocrit();
        this.assertHematocrit(hematocrit);

    }

    private void assertBloodPressure(ClinicalObservationBo clinicalObservationBo) {
        if (clinicalObservationBo == null)
            return;
        int value = Integer.parseInt(clinicalObservationBo.getValue());

        if (value >= MIN_BLOOD && value <= MAX_BLOOD)
            return;

        throw new ConstraintViolationException("blood-pressure-value-not-valid", Collections.emptySet());
    }

    private void assertHematocrit(ClinicalObservationBo clinicalObservationBo) {
        if (clinicalObservationBo == null)
            return;
        double value = Double.parseDouble(clinicalObservationBo.getValue());

        if (value >= MIN_PERCENTAGE && value <= MAX_PERCENTAGE)
            return;

        throw new ConstraintViolationException("hematocrit-value-not-valid", Collections.emptySet());
    }

}
