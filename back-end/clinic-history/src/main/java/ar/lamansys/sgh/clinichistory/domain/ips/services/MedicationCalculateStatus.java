package ar.lamansys.sgh.clinichistory.domain.ips.services;

import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
public class MedicationCalculateStatus {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationCalculateStatus.class);

    private final DateTimeProvider dateTimeProvider;

    public MedicationCalculateStatus(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    public String execute(String statusId, DosageBo dosage){
        Assert.notNull(statusId, "El estado del medicamento es obligatorio para el calculo");
        Assert.notNull(dosage, "La dosis es obligatoria para el calculo");

        if (isActive(statusId, dosage))
            return MedicationStatementStatus.ACTIVE;
        if (isSuspended(statusId, dosage))
            return MedicationStatementStatus.SUSPENDED;
        if (isStopped(statusId, dosage))
            return MedicationStatementStatus.STOPPED;
        return null;
    }


    private boolean isActive(String statusId, DosageBo dosage) {
        if (MedicationStatementStatus.SUSPENDED.equals(statusId) && !isSuspended(statusId, dosage))
            return true;
        if (MedicationStatementStatus.STOPPED.equals(statusId) && !isStopped(statusId, dosage))
            return true;
        return MedicationStatementStatus.ACTIVE.equals(statusId);
    }

    private boolean isStopped(String statusId, DosageBo dosage) {
        if (dosage.getId() == null && !MedicationStatementStatus.STOPPED.equals(statusId))
            return false;
        if (dosage.getId() == null)
            return true;
        return dosage.getEndDate() != null && dateTimeProvider.nowDate().isAfter(dosage.getEndDate().toLocalDate());
    }

    private boolean isSuspended(String statusId, DosageBo dosage) {
        if (dosage.getId() == null && !MedicationStatementStatus.SUSPENDED.equals(statusId))
            return false;
        if (dosage.getId() == null)
            return true;
        return dosage.getSuspendedEndDate() != null &&  !dateTimeProvider.nowDate().isAfter(dosage.getSuspendedEndDate());
    }
}
