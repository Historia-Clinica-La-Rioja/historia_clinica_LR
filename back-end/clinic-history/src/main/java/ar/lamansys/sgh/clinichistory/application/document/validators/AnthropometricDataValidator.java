package ar.lamansys.sgh.clinichistory.application.document.validators;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.util.Collections;

@Slf4j
@Component
public class AnthropometricDataValidator {

    private static final Integer MIN_HEIGHT_VALUE = 0;
    private static final Integer MAX_HEIGHT_VALUE = 1000;

    private static final Double MIN_WEIGHT_VALUE = 0.0;
    private static final Double MAX_WEIGHT_VALUE = 1000.0;

    public boolean isValid(IDocumentBo IDocumentBo){
        log.debug("Input parameters -> document {}", IDocumentBo);
        AnthropometricDataBo anthropometricDataBo = IDocumentBo.getAnthropometricData();
        if (anthropometricDataBo == null)
            return true;

        validWeightClinicalObservation(anthropometricDataBo.getWeight());
        validHeightClinicalObservation(anthropometricDataBo.getHeight());

        return true;
    }

    private void validHeightClinicalObservation(ClinicalObservationBo clinicalObservationBo) {
        if (clinicalObservationBo == null || clinicalObservationBo.getValue() == null)
            return;
        int anthropometricValue = Integer.parseInt(clinicalObservationBo.getValue());
        if (anthropometricValue >= MIN_HEIGHT_VALUE && anthropometricValue <= MAX_HEIGHT_VALUE)
            return;
        throw new ConstraintViolationException(String.format("%s: La medición debe estar entre %s y %s", "altura", MIN_HEIGHT_VALUE, MAX_HEIGHT_VALUE), Collections.emptySet());
    }

    private void validWeightClinicalObservation(ClinicalObservationBo clinicalObservationBo) {
        if (clinicalObservationBo == null || clinicalObservationBo.getValue() == null)
            return;
        double anthropometricValue = Double.parseDouble(clinicalObservationBo.getValue());
        if (anthropometricValue >= MIN_WEIGHT_VALUE && anthropometricValue <= MAX_WEIGHT_VALUE)
            return;
        throw new ConstraintViolationException(String.format("%s: La medición debe estar entre %s y %s", "peso", MIN_WEIGHT_VALUE, MAX_WEIGHT_VALUE), Collections.emptySet());
    }
}