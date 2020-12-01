package net.pladema.clinichistory.hospitalization.service.documents.validation;

import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.documents.service.ips.domain.AnthropometricDataBo;
import net.pladema.clinichistory.documents.service.ips.domain.ClinicalObservationBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolationException;
import java.util.Collections;


public class AnthropometricDataValidator {

    private static final Logger LOG = LoggerFactory.getLogger(AnthropometricDataValidator.class);

    private static final Integer MIN_VALUE = 0;
    private static final Integer MAX_VALUE = 1000;

    public boolean isValid(Document document){
        LOG.debug("Input parameters -> document {}", document);
        AnthropometricDataBo anthropometricDataBo = document.getAnthropometricData();
        if (anthropometricDataBo == null)
            return true;

        validClinicalObservation(anthropometricDataBo.getWeight(), "peso");
        validClinicalObservation(anthropometricDataBo.getHeight(), "altura");

        return true;
    }

    private void validClinicalObservation(ClinicalObservationBo clinicalObservationBo, String property) {
        if (clinicalObservationBo == null)
            return;
        int anthropometricValue = Integer.parseInt(clinicalObservationBo.getValue());
        if (anthropometricValue >= MIN_VALUE && anthropometricValue <= MAX_VALUE)
            return;
        throw new ConstraintViolationException(String.format("%s: La medición debe estar entre %s y %s", property, MIN_VALUE, MAX_VALUE), Collections.emptySet());
    }
}