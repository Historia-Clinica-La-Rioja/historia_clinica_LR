package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class DosageValidator {

    private static final Logger LOG = LoggerFactory.getLogger(DosageValidator.class);

    public boolean isValid(DosageBo dosage) {
        LOG.debug("Input parameters -> dosage {}", dosage);
        if (dosage == null)
            return true;
        Assert.notNull(dosage.getStartDate(), "La fecha de comienzo para tomar la medicación es obligatoria");
        if (!dosage.isChronic())
            Assert.notNull(dosage.getDuration(), "Sí la medicación no es crónica se necesita saber durante cuantos días de debe tomar");
        return true;
    }


}