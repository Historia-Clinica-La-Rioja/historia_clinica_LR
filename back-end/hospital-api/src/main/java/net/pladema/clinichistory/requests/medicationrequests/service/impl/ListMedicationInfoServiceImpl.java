package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.services.MedicationCalculateStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import net.pladema.clinichistory.requests.medicationrequests.repository.ListMedicationRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.domain.MedicationFilterVo;
import net.pladema.clinichistory.requests.medicationrequests.service.ListMedicationInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationFilterBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListMedicationInfoServiceImpl implements ListMedicationInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(ListMedicationInfoServiceImpl.class);

    private final ListMedicationRepository listMedicationRepository;

    private final MedicationCalculateStatus medicationCalculateStatus;

    public ListMedicationInfoServiceImpl(ListMedicationRepository listMedicationRepository,
                                         MedicationCalculateStatus medicationCalculateStatus) {
        this.listMedicationRepository = listMedicationRepository;
        this.medicationCalculateStatus = medicationCalculateStatus;
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

        result.setSnomed(new SnomedBo((Integer) row[1], (String) row[2],(String) row[3]));

        result.setStatusId((String) row[4]);
        result.setStatus((String) row[5]);

        HealthConditionBo healthConditionBo = new HealthConditionBo();
        healthConditionBo.setId((Integer) row[6]);
        healthConditionBo.setSnomed(new SnomedBo((Integer) row[7], (String) row[8], (String) row[9]));
        result.setHealthCondition(healthConditionBo);


        result.setNote((String) row[10]);

        DosageBo d = new DosageBo();
        if (row[11] != null) {
            d.setId((Integer) row[11]);
            d.setDuration((Double) row[12]);
            d.setFrequency((Integer) row[13]);
            d.setPeriodUnit(row[14] != null ? EUnitsOfTimeBo.map((String) row[14]) : null);
            d.setChronic((Boolean) row[15]);
            d.setStartDate(row[16] != null ? ((Timestamp) row[16]).toLocalDateTime() : null);
            d.setEndDate(row[17] != null ? ((Timestamp) row[17]).toLocalDateTime() : null);
            d.setSuspendedStartDate(row[18] != null ? ((Date) row[18]).toLocalDate() : null);
            d.setSuspendedEndDate(row[19] != null ? ((Date) row[19]).toLocalDate() : null);

        }
        result.setDosage(d);
        result.setEncounterId((Integer)row[20]);
        result.setHasRecipe(row[21] != null && (Boolean)row[21]);
        result.setSuspended(MedicationStatementStatus.SUSPENDED.equals(medicationCalculateStatus.execute(result.getStatusId(), result.getDosage())));

        result.setUserId((Integer) row[22]);
        result.setCreatedOn(row[23] != null ? ((Timestamp) row[23]).toLocalDateTime().toLocalDate() : null);
        LOG.trace("OUTPUT -> {}", result);
        return result;
    }


    private boolean byStatus(MedicationBo medicationBo, String filterStatusId) {
        String computedStatus = medicationCalculateStatus.execute(medicationBo.getStatusId(), medicationBo.getDosage());
        medicationBo.setStatusId(computedStatus);
        if (MedicationStatementStatus.SUSPENDED.equals(filterStatusId))
            return MedicationStatementStatus.SUSPENDED.equals(computedStatus);
        if (MedicationStatementStatus.STOPPED.equals(filterStatusId))
            return MedicationStatementStatus.STOPPED.equals(computedStatus);
        if (MedicationStatementStatus.ACTIVE.equals(filterStatusId))
            return MedicationStatementStatus.ACTIVE.equals(computedStatus);
        return filterStatusId == null;
    }
}
