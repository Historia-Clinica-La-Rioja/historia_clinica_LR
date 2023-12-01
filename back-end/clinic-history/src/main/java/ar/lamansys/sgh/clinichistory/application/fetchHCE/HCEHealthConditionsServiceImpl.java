package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsSameUserIdFromHealthCondition;
import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsWithinExpirationTimeLimit;
import ar.lamansys.sgh.clinichistory.application.ports.HCEReferenceCounterReferenceStorage;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEHospitalizationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEPersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEReferenceProblemBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.HCEHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHospitalizationVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HCEHealthConditionsServiceImpl implements HCEHealthConditionsService {

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEHealthConditionRepository hceHealthConditionRepository;
    private final HCEReferenceCounterReferenceStorage hceReferenceCounterReferenceStorage;
    
    private final IsSameUserIdFromHealthCondition isSameUserIdFromHealthCondition;
    
    private final IsWithinExpirationTimeLimit isWithinExpirationTimeLimit;

	private final SharedLoggedUserPort sharedLoggedUserPort;

    @Override
    public List<HCEPersonalHistoryBo> getActivePersonalHistories(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream()
                .map(HCEPersonalHistoryBo::new)
                .filter(HCEPersonalHistoryBo::isActive)
                .peek(hcePersonalHistoryBo -> hcePersonalHistoryBo.setCanBeMarkAsError(this.canBeMarkAsError(hcePersonalHistoryBo)))
				.sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

	@Override
	public List<HCEPersonalHistoryBo> getActivePersonalHistoriesByUser(Integer patientId, Integer userId) {
		log.debug(LOGGING_INPUT, patientId);
		List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistoriesByUser(patientId, userId);
		List<HCEPersonalHistoryBo> result = resultQuery.stream()
				.map(HCEPersonalHistoryBo::new)
				.filter(HCEPersonalHistoryBo::isActive)
                .peek(hcePersonalHistoryBo -> hcePersonalHistoryBo.setCanBeMarkAsError(this.canBeMarkAsError(hcePersonalHistoryBo)))
				.sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
				.collect(Collectors.toList());
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

    @Override
    public List<HCEPersonalHistoryBo> getFamilyHistories(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getFamilyHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new).collect(Collectors.toList());
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getChronicConditions(Integer institutionId, Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream()
                .map(HCEPersonalHistoryBo::new)
                .filter(HCEPersonalHistoryBo::isChronic)
                .peek(hcePersonalHistoryBo -> hcePersonalHistoryBo.setCanBeMarkAsError(this.canBeMarkAsError(hcePersonalHistoryBo)))
                .sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());

		List<Short> loggedUserRoleIds = sharedLoggedUserPort.getLoggedUserRoleIds(institutionId, UserInfo.getCurrentAuditor());
        List<HCEReferenceProblemBo> problemsWithReferences = hceReferenceCounterReferenceStorage.getProblemsWithReferences(patientId, loggedUserRoleIds);
        result.forEach(p -> problemsWithReferences.forEach(pwr -> {
			if (p.getSnomedSctid().equals(pwr.getSnomedSctid()) && p.getSnomedPt().equals(pwr.getSnomedPt()))
				p.setHasPendingReference(true);
		}));

        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getActiveProblems(Integer institutionId, Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new)
                .filter(HCEPersonalHistoryBo::isActiveProblem)
                .peek(hcePersonalHistoryBo -> hcePersonalHistoryBo.setCanBeMarkAsError(this.canBeMarkAsError(hcePersonalHistoryBo)))
                .sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());

		List<Short> loggedUserRoleIds = sharedLoggedUserPort.getLoggedUserRoleIds(institutionId, UserInfo.getCurrentAuditor());
        List<HCEReferenceProblemBo> problemsWithReferences = hceReferenceCounterReferenceStorage.getProblemsWithReferences(patientId, loggedUserRoleIds);
        result.forEach(p -> problemsWithReferences.forEach(pwr -> {
			if (p.getSnomedSctid().equals(pwr.getSnomedSctid()) && p.getSnomedPt().equals(pwr.getSnomedPt()))
				p.setHasPendingReference(true);
		}));

        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getSolvedProblems(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new).filter(HCEPersonalHistoryBo::isSolvedProblem)
                .sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate).reversed()).collect(Collectors.toList());
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getProblemsAndChronicConditionsMarkedAsError(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEPersonalHistoryBo> result = hceHealthConditionRepository.getPersonalHistories(patientId).stream()
                .map(HCEPersonalHistoryBo::new)
                .filter(HCEPersonalHistoryBo::isMarkedAsError)
                .sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate).reversed())
                .collect(Collectors.toList());
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEHospitalizationBo> getHospitalizationHistory(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHospitalizationVo> resultQuery = hceHealthConditionRepository.getHospitalizationHistory(patientId);
        List<HCEHospitalizationBo> result = resultQuery.stream().map(HCEHospitalizationBo::new).collect(Collectors.toList());
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

	@Override
	public List<HCEHospitalizationBo> getEmergencyCareHistory(Integer patientId) {
		log.debug(LOGGING_INPUT, patientId);
		List<HCEHospitalizationVo> resultQuery = hceHealthConditionRepository.getEmergencyCareHistory(patientId);
		List<HCEHospitalizationBo> result = resultQuery.stream().map(HCEHospitalizationBo::new).collect(Collectors.toList());
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

    private Boolean canBeMarkAsError(HCEPersonalHistoryBo hcePersonalHistoryBo) {
        return isSameUserIdFromHealthCondition.run(hcePersonalHistoryBo.getId())
                && isWithinExpirationTimeLimit.run(hcePersonalHistoryBo.getId());
    }
}
