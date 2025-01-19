package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.GetDiagnosticReportInfoRepository;
import net.pladema.clinichistory.requests.servicerequests.service.DiagnosticReportInfoService;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FileBo;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiagnosticReportInfoServiceImpl implements DiagnosticReportInfoService {

    public static final String OUTPUT = "Output -> {}";
    private final GetDiagnosticReportInfoRepository getDiagnosticReportInfoRepository;

	@Override
	public DiagnosticReportBo getByAppointmentId(Integer appointmentId) {
		log.debug("input -> appointmentId {}", appointmentId);
		Object[] queryResult = getDiagnosticReportInfoRepository.getDiagnosticReportByAppointmentId(appointmentId);
		var result = queryResult != null ? createDiagnosticReportBo(queryResult, null) : null;
		log.debug("Output -> {}", result);
		return result;
	}

    private DiagnosticReportBo createDiagnosticReportBo(Object[] row, List<FileBo> filesBo) {
        log.debug("Input parameters -> row {}", row);
        DiagnosticReportBo result = new DiagnosticReportBo();
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

		if (filesBo != null)
        	result.setFiles(filesBo);

        return result;
    }
}
