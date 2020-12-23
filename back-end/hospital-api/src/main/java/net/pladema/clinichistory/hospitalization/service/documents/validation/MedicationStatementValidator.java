package net.pladema.clinichistory.hospitalization.service.documents.validation;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class MedicationStatementValidator {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationStatementValidator.class);

    public boolean isValid(String statusId) {
        LOG.debug("Input parameters -> statusId {}", statusId);
        Assert.notNull(statusId, "El estado es obligatorio");
        Assert.isTrue(MedicationStatementStatus.STATES.contains(statusId), "El estado de la medicación es invalido");
        return true;
    }


}