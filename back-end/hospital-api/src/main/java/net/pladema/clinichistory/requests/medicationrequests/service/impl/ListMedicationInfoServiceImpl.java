package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.MedicationStatementStatus;
import net.pladema.clinichistory.documents.service.ips.domain.DosageBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;
import net.pladema.clinichistory.requests.medicationrequests.repository.ListMedicationRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.domain.MedicationFilterVo;
import net.pladema.clinichistory.requests.medicationrequests.service.ListMedicationInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationFilterBo;
import net.pladema.sgx.dates.configuration.DateTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListMedicationInfoServiceImpl implements ListMedicationInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(ListMedicationInfoServiceImpl.class);

    private final ListMedicationRepository listMedicationRepository;

    private final DateTimeProvider dateTimeProvider;

    public ListMedicationInfoServiceImpl(ListMedicationRepository listMedicationRepository, DateTimeProvider dateTimeProvider) {
        this.listMedicationRepository = listMedicationRepository;
        this.dateTimeProvider = dateTimeProvider;
    }


    @Override
    public List<MedicationBo> execute(MedicationFilterBo filter) {
        LOG.debug("Input parameters -> filter {}", filter);
        var filterVo = new MedicationFilterVo(filter.getPatientId(), filter.getStatusId(),
                filter.getMedicationStatement(), filter.getHealthCondition());
        List<MedicationBo> result = listMedicationRepository.execute(filterVo).stream()
                .map(this::createMedicationBo)
                .filter(mb -> byStatus(mb, filter.getStatusId()))
                .collect(Collectors.toList());
        LOG.trace("OUTPUT List -> {}", result);
        return result;
    }

    private MedicationBo createMedicationBo(Object[] row) {
        LOG.debug("Input parameters -> row {}", row);
        MedicationBo result = new MedicationBo();
        result.setId((Integer)row[0]);

        result.setSnomed(new SnomedBo((String) row[1],(String) row[2]));

        result.setStatusId((String) row[3]);
        result.setStatus((String) row[4]);

        HealthConditionBo healthConditionBo = new HealthConditionBo();
        healthConditionBo.setId((Integer) row[5]);
        healthConditionBo.setSnomed(new SnomedBo((String) row[6], (String) row[7]));
        result.setHealthCondition(healthConditionBo);


        result.setNote((String) row[8]);

        if (row[9] != null) {
            DosageBo d = new DosageBo();
            d.setId((Integer) row[9]);
            d.setDuration((Double) row[10]);
            d.setFrequency((Integer) row[11]);
            d.setPeriodUnit(row[12] != null ? EUnitsOfTimeBo.map((String) row[12]) : null);
            d.setChronic((Boolean) row[13]);
            d.setStartDate(row[14] != null ? ((Date) row[14]).toLocalDate() : null);
            d.setSuspendedStartDate(row[15] != null ? ((Date) row[15]).toLocalDate() : null);
            d.setSuspendedEndDate(row[15] != null ? ((Date) row[16]).toLocalDate() : null);
            result.setDosage(d);
        }

        result.setEncounterId((Integer)row[17]);
        result.setHasRecipe(row[18] != null && (Boolean)row[18]);
        result.setSuspended(isSuspended(result.getStatusId(), result.getDosage()));

        result.setUserId((Integer) row[19]);
        LOG.trace("OUTPUT -> {}", result);
        return result;
    }

    private boolean byStatus(MedicationBo medicationBo, String filterStatusId) {
        if (MedicationStatementStatus.SUSPENDED.equals(filterStatusId))
            return isSuspended(medicationBo.getStatusId(), medicationBo.getDosage());
        if (MedicationStatementStatus.STOPPED.equals(filterStatusId))
            return isStopped(medicationBo.getStatusId(), medicationBo.getDosage());
        if (MedicationStatementStatus.ACTIVE.equals(filterStatusId))
            return isActive(medicationBo.getStatusId(), medicationBo.getDosage());
        return false;
    }

    private boolean isActive(String statusId, DosageBo dosage) {
        if (MedicationStatementStatus.SUSPENDED.equals(statusId) && !isSuspended(statusId, dosage))
            return true;
        if (MedicationStatementStatus.STOPPED.equals(statusId) && !isStopped(statusId, dosage))
            return true;
        return MedicationStatementStatus.ACTIVE.equals(statusId);
    }

    private boolean isStopped(String statusId, DosageBo dosage) {
        if (dosage == null && !MedicationStatementStatus.STOPPED.equals(statusId))
            return false;
        if (dosage == null)
            return true;
        return dosage.getEndDate() != null && dateTimeProvider.nowDate().isAfter(dosage.getEndDate());
    }

    private boolean isSuspended(String statusId, DosageBo dosage) {
        if (dosage == null && !MedicationStatementStatus.SUSPENDED.equals(statusId))
            return false;
        if (dosage == null)
            return true;
        return dosage.getSuspendedEndDate() != null &&  dateTimeProvider.nowDate().isBefore(dosage.getSuspendedEndDate());
    }
}
