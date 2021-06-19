package net.pladema.clinichistory.hospitalization.service.documents.validation;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class PatientInfoValidator {

    private static final Logger LOG = LoggerFactory.getLogger(PatientInfoValidator.class);

    public boolean isValid(PatientInfoBo patient) {
        LOG.debug("Input parameters -> patient {}", patient);
        Assert.notNull(patient, "La información del paciente es obligatoria");
        Assert.notNull(patient.getId(), "El código identificador del paciente es obligatorio");
        return true;
    }

}
