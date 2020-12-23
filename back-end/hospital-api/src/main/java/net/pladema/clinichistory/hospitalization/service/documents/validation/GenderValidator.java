package net.pladema.clinichistory.hospitalization.service.documents.validation;

import net.pladema.person.repository.entity.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class GenderValidator {

    private static final Logger LOG = LoggerFactory.getLogger(GenderValidator.class);

    public boolean isValid(Short genderId) {
        LOG.debug("Input parameters -> genderId {}", genderId);
        Assert.notNull(genderId, "El genero del paciente es obligatorio");
        Assert.isTrue(Gender.GENDERS.contains(genderId), "El genero es invalido");
        return true;
    }
}

