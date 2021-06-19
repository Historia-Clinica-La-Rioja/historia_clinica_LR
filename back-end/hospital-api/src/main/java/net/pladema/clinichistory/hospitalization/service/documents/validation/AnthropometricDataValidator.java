package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalObservationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;
import java.util.Collections;


public class AnthropometricDataValidator {

    private static final Logger LOG = LoggerFactory.getLogger(AnthropometricDataValidator.class);

    private static final Integer MIN_HEIGHT_VALUE = 0;
    private static final Integer MAX_HEIGHT_VALUE = 1000;

    private static final Double MIN_WEIGHT_VALUE = 0.0;
    private static final Double MAX_WEIGHT_VALUE = 1000.0;

    public boolean isValid(IDocumentBo IDocumentBo){
        LOG.debug("Input parameters -> document {}", IDocumentBo);
        AnthropometricDataBo anthropometricDataBo = IDocumentBo.getAnthropometricData();
        if (anthropometricDataBo == null)
            return true;

        validWeightClinicalObservation(anthropometricDataBo.getWeight(), "peso");
        validHeightClinicalObservation(anthropometricDataBo.getHeight(), "altura");

        return true;
    }

    private void validHeightClinicalObservation(ClinicalObservationBo clinicalObservationBo, String property) {
        if (clinicalObservationBo == null)
            return;
        int anthropometricValue = Integer.parseInt(clinicalObservationBo.getValue());
        if (anthropometricValue >= MIN_HEIGHT_VALUE && anthropometricValue <= MAX_HEIGHT_VALUE)
            return;
        throw new ConstraintViolationException(String.format("%s: La medición debe estar entre %s y %s", property, MIN_HEIGHT_VALUE, MAX_HEIGHT_VALUE), Collections.emptySet());
    }

    private void validWeightClinicalObservation(ClinicalObservationBo clinicalObservationBo, String property) {
        if (clinicalObservationBo == null)
            return;
        double anthropometricValue = Double.parseDouble(clinicalObservationBo.getValue());
        if (anthropometricValue >= MIN_WEIGHT_VALUE && anthropometricValue <= MAX_WEIGHT_VALUE)
            return;
        throw new ConstraintViolationException(String.format("%s: La medición debe estar entre %s y %s", property, MIN_WEIGHT_VALUE, MAX_WEIGHT_VALUE), Collections.emptySet());
    }
}