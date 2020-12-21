package net.pladema.clinichistory.requests.servicerequests.service.impl;

import net.pladema.clinichistory.documents.service.ips.domain.DosageBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;
import net.pladema.clinichistory.requests.medicationrequests.service.impl.ListMedicationInfoServiceImpl;
import net.pladema.clinichistory.requests.servicerequests.repository.ListDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.DiagnosticReportFilterVo;
import net.pladema.clinichistory.requests.servicerequests.service.ListDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListDiagnosticReportInfoServiceImpl implements ListDiagnosticReportInfoService {

    ListDiagnosticReportRepository listDiagnosticReportRepository;
    private static final Logger LOG = LoggerFactory.getLogger(ListDiagnosticReportInfoServiceImpl.class);

    public ListDiagnosticReportInfoServiceImpl(ListDiagnosticReportRepository listDiagnosticReportRepository) {
        this.listDiagnosticReportRepository = listDiagnosticReportRepository;
    }

    @Override
    public List<DiagnosticReportBo> execute(DiagnosticReportFilterBo filter) {
        DiagnosticReportFilterVo filterVo = new DiagnosticReportFilterVo(
                filter.getPatientId(),
                filter.getStatus(),
                filter.getServiceRequest(),
                filter.getHealthCondition()
        );
        List<DiagnosticReportBo> result = listDiagnosticReportRepository.execute(filterVo).stream()
                .map(this::createMedicationBo)
                .collect(Collectors.toList());
        LOG.trace("OUTPUT List -> {}", result);
        return result;
    }

    private DiagnosticReportBo createMedicationBo(Object[] row) {
        LOG.debug("Input parameters -> row {}", row);
        DiagnosticReportBo result = new DiagnosticReportBo();
        result.setId((Integer) row[0]);
        result.setSnomed(new SnomedBo((Integer) row[1], (String)  row[11], (String) row[2]));

        result.setStatusId((String) row[3]);


        result.setStatus((String) row[4]);

        HealthConditionBo healthConditionBo = new HealthConditionBo();
        healthConditionBo.setId((Integer) row[5]);
        healthConditionBo.setSnomed(new SnomedBo((Integer) row[6], (String) row[12], (String) row[7]));
        result.setHealthCondition(healthConditionBo);

        result.setObservations((String) row[8]);

        result.setEncounterId((Integer) row[9]);

        result.setUserId((Integer) row[10]);
        LOG.trace("OUTPUT -> {}", result);
        return result;
    }
}