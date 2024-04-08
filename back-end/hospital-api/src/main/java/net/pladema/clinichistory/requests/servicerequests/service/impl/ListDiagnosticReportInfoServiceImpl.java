package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.AllArgsConstructor;
import net.pladema.clinichistory.requests.servicerequests.repository.ListDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.DiagnosticReportFilterVo;
import net.pladema.clinichistory.requests.servicerequests.service.ListDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ListDiagnosticReportInfoServiceImpl implements ListDiagnosticReportInfoService {

    private ListDiagnosticReportRepository listDiagnosticReportRepository;

	private SharedLoggedUserPort sharedLoggedUserPort;

    private static final Logger LOG = LoggerFactory.getLogger(ListDiagnosticReportInfoServiceImpl.class);
    private static final String OUTPUT = "create result -> {}";

    @Override
    public List<DiagnosticReportBo> getList(DiagnosticReportFilterBo filter) {
        LOG.trace("Input parameters -> filter {}", filter);
        DiagnosticReportFilterVo filterVo = new DiagnosticReportFilterVo(
                filter.getPatientId(), filter.getStatus(), filter.getStudy(), filter.getHealthCondition(),
                filter.getCategory(), filter.getCategoriesToBeExcluded(), null, null
        );
        List<DiagnosticReportBo> result = parseDiagnosticReportBoList(listDiagnosticReportRepository.getList(filterVo));
        LOG.trace("OUTPUT List -> {}", result);
        return result;
    }

	@Override
	public List<DiagnosticReportBo> getMedicalOrderList(DiagnosticReportFilterBo filter) {
		LOG.trace("Input parameters -> filter {}", filter);
		DiagnosticReportFilterVo filterVo = new DiagnosticReportFilterVo(filter.getPatientId(), filter.getStatus(), null, null, filter.getCategory(), null, null, null);
		List<DiagnosticReportBo> result = parseDiagnosticReportBoList(listDiagnosticReportRepository.getMedicalOrderList(filterVo));
		LOG.trace("OUTPUT List -> {}", result);
		return result;
	}

	@Override
	public List<DiagnosticReportBo> getListIncludingConfidential(DiagnosticReportFilterBo filter, Integer institutionId) {
		LOG.trace("Input parameters -> filter {}, institutionId {}", filter, institutionId);
		Integer userId = UserInfo.getCurrentAuditor();
		List<Short> loggedUserRoles = sharedLoggedUserPort.getLoggedUserRoleIds(institutionId, userId);
		DiagnosticReportFilterVo filterVo = new DiagnosticReportFilterVo(filter.getPatientId(), filter.getStatus(), filter.getStudy(), filter.getHealthCondition(),
				filter.getCategory(), filter.getCategoriesToBeExcluded(), userId, loggedUserRoles);
		List<DiagnosticReportBo> result = parseDiagnosticReportBoList(listDiagnosticReportRepository.getList(filterVo));
		LOG.trace("OUTPUT List -> {}", result);
		return result;
	}

	private List<DiagnosticReportBo> parseDiagnosticReportBoList(List<Object[]> listDiagnosticReportRepository) {
		return listDiagnosticReportRepository.stream().map(this::createDiagnosticReportBo).collect(Collectors.toList());
	}

	private DiagnosticReportBo createDiagnosticReportBo(Object[] row) {
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
        result.setEffectiveTime(row[13] != null ? ((Timestamp) row[13]).toLocalDateTime() : null);
		result.setCategory((String) row[14]);
		result.setSource((String) row[15]);
		result.setSourceId((Integer) row[16]);
        LOG.trace(OUTPUT, result);
        return result;
    }
}