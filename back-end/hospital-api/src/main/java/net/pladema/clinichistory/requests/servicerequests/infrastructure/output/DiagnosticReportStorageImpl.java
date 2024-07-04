package net.pladema.clinichistory.requests.servicerequests.infrastructure.output;

import ar.lamansys.sgh.clinichistory.domain.ips.FileBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.application.port.DiagnosticReportStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.DiagnosticReportResultsBo;
import net.pladema.clinichistory.requests.servicerequests.repository.DiagnosticReportFileRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.GetDiagnosticReportInfoRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.FileVo;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DiagnosticReportStorageImpl implements DiagnosticReportStorage {

    private final GetDiagnosticReportInfoRepository getDiagnosticReportInfoRepository;
    private final DiagnosticReportFileRepository diagnosticReportFileRepository;

    @Override
    public DiagnosticReportResultsBo getDiagnosticReportResults(Integer diagnosticReportId) {
        Object[] rowResult = getDiagnosticReportInfoRepository.execute(diagnosticReportId);
        return this.createDiagnosticReportResultsBo(rowResult);
    }

    @Override
    public Optional<List<FileBo>> getFilesByDiagnosticReport(Integer diagnosticReportId) {
        return Optional.of(diagnosticReportFileRepository.getFilesByDiagnosticReport(diagnosticReportId).stream()
                .map(this::mapFile)
                .collect(Collectors.toList()));
    }

    private DiagnosticReportResultsBo createDiagnosticReportResultsBo(Object[] row) {
        DiagnosticReportResultsBo result = new DiagnosticReportResultsBo();
        result.setId((Integer) row[0]);
        result.setSnomed(new SnomedBo((Integer) row[1], (String)  row[2], (String) row[3]));
        result.setHealthConditionId((Integer) row[4]);

        HealthConditionBo healthConditionBo = new HealthConditionBo();
        healthConditionBo.setId((Integer) row[4]);
        healthConditionBo.setSnomed(new SnomedBo((Integer) row[5], (String) row[6], (String) row[7]));
        result.setHealthCondition(healthConditionBo);

        result.setObservations((String) row[8]);
        result.setStatusId((String) row[9]);
        result.setEncounterId((Integer) row[10]);
        result.setEffectiveTime(row[11] != null ? ((Timestamp) row[11]).toLocalDateTime() : null);
        result.setUserId((Integer) row[12]);
        result.setObservationsFromServiceRequest((String) row[13]);

        return result;
    }

    private FileBo mapFile(FileVo fileVo) {
        return new FileBo(fileVo.getFileId(), fileVo.getFileName());
    }
}
