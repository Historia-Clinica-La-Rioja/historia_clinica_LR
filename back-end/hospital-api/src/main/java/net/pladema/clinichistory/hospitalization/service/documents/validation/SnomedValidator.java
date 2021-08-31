package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class SnomedValidator {

    private static final Logger LOG = LoggerFactory.getLogger(SnomedValidator.class);

    public boolean isValid(SnomedBo snomed) {
        LOG.debug("Input parameters -> snomed {}", snomed);
        Assert.notNull(snomed, "La terminología snomed es obligatoria");
        Assert.notNull(snomed.getSctid(), "El código identificador de snomed es obligatorio");
        Assert.notNull(snomed.getPt(), "El termino preferido de snomed es obligatorio");
        return true;
    }


}