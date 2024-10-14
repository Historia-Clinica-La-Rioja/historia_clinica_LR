package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.application.GetDiagnosticReportResultsList;
import net.pladema.clinichistory.requests.servicerequests.domain.DiagnosticReportResultsBo;
import net.pladema.clinichistory.requests.servicerequests.repository.ListDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.domain.DiagnosticReportFilterVo;
import net.pladema.clinichistory.requests.servicerequests.service.ListDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ListDiagnosticReportInfoServiceImpl implements ListDiagnosticReportInfoService {
    
    private static final String OUTPUT = "create result -> {}";

    private final ListDiagnosticReportRepository listDiagnosticReportRepository;
	private final SharedLoggedUserPort sharedLoggedUserPort;
    private final GetDiagnosticReportResultsList getDiagnosticReportResultsList;

	@Override
	public List<DiagnosticReportBo> getMedicalOrderList(DiagnosticReportFilterBo filter) {
		log.trace("Input parameters -> filter {}", filter);
		DiagnosticReportFilterVo filterVo = new DiagnosticReportFilterVo(filter.getPatientId(), filter.getStatus(), null, null, filter.getCategory(), null, null, null);
		List<DiagnosticReportBo> result = parseDiagnosticReportBoList(listDiagnosticReportRepository.getMedicalOrderList(filterVo));
		log.trace("OUTPUT List -> {}", result);
		return result;
	}

	@Override
	public List<DiagnosticReportResultsBo> getListIncludingConfidential(DiagnosticReportFilterBo filter, Integer institutionId) {
		log.trace("Input parameters -> filter {}, institutionId {}", filter, institutionId);
        this.includeConfidentialFilters(filter, institutionId);
        List<DiagnosticReportResultsBo> result = getDiagnosticReportResultsList.run(filter);
		log.trace("OUTPUT List -> {}", result);
		return result;
	}

    private void includeConfidentialFilters(DiagnosticReportFilterBo filter, Integer institutionId) {
        Integer userId = UserInfo.getCurrentAuditor();
        List<Short> loggedUserRoles = sharedLoggedUserPort.getLoggedUserRoleIds(institutionId, userId);
        filter.setUserId(userId);
        filter.setLoggedUserRoleIds(loggedUserRoles);
    }

    private List<DiagnosticReportBo> parseDiagnosticReportBoList(List<Object[]> listDiagnosticReportRepository) {
		return listDiagnosticReportRepository.stream().map(this::createDiagnosticReportBo).collect(Collectors.toList());
	}

	private DiagnosticReportBo createDiagnosticReportBo(Object[] row) {
        log.debug("Input parameters -> row {}", row);
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
        log.trace(OUTPUT, result);
        return result;
    }
}