package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportFileRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.GetDiagnosticReportInfoRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo;
import net.pladema.clinichistory.requests.servicerequests.service.DiagnosticReportInfoService;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FileBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiagnosticReportInfoServiceImpl implements DiagnosticReportInfoService {

    private final GetDiagnosticReportInfoRepository getDiagnosticReportInfoRepository;
    private final DiagnosticReportFileRepository diagnosticReportFileRepository;
    private static final Logger LOG = LoggerFactory.getLogger(DiagnosticReportInfoServiceImpl.class);
    public static final String OUTPUT = "Output -> {}";

    public DiagnosticReportInfoServiceImpl(GetDiagnosticReportInfoRepository getDiagnosticReportInfoRepository,
                                           DiagnosticReportFileRepository diagnosticReportFileRepository) {
        this.getDiagnosticReportInfoRepository = getDiagnosticReportInfoRepository;
        this.diagnosticReportFileRepository = diagnosticReportFileRepository;
    }

    @Override
    public DiagnosticReportBo run(Integer diagnosticReportId) {
        LOG.debug("input -> diagnosticReportId {}", diagnosticReportId);
        var result = createDiagnosticReportBo(
                getDiagnosticReportInfoRepository.execute(diagnosticReportId),
                diagnosticReportFileRepository.getFilesByDiagnosticReport(diagnosticReportId).stream()
                        .map(this::mapFile)
                        .collect(Collectors.toList())
                );
        LOG.debug(OUTPUT, result);
        return result;
    }


    private DiagnosticReportBo createDiagnosticReportBo(Object[] row, List<FileBo> filesBo) {
        LOG.debug("Input parameters -> row {}", row);
        DiagnosticReportBo result = new DiagnosticReportBo();
        result.setId((Integer) row[0]);
        result.setSnomed(new SnomedBo((Integer) row[1], (String)  row[2], (String) row[3]));


        HealthConditionBo healthConditionBo = new HealthConditionBo();
        healthConditionBo.setId((Integer) row[4]);
        healthConditionBo.setSnomed(new SnomedBo((Integer) row[5], (String) row[6], (String) row[7]));
        result.setHealthCondition(healthConditionBo);

        result.setObservations((String) row[8]);

        result.setStatusId((String) row[9]);

        result.setEncounterId((Integer) row[10]);

        result.setEffectiveTime(row[11] != null ? ((Timestamp) row[11]).toLocalDateTime() : null);

        result.setUserId((Integer) row[12]);

        result.setFiles(filesBo);

        LOG.trace(OUTPUT, result);

        return result;
    }

    public FileBo mapFile(FileVo fileVo){
        return new FileBo(fileVo.getFileId(), fileVo.getFileName());
    }
}
