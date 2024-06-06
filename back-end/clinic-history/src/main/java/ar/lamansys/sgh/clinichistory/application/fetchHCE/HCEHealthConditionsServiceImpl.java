package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsSameUserIdFromHealthCondition;
import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsWithinExpirationTimeLimit;
import ar.lamansys.sgh.clinichistory.application.ports.HCEReferenceCounterReferenceStorage;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEHospitalizationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEPersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEReferenceProblemBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.HCEHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHospitalizationVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedLoggedUserPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEPersonalHistoryVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
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

    private final SharedPersonPort sharedPersonPort;

    @Override
    public List<HCEHealthConditionBo> getSummaryProblems(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getSummaryProblems(patientId);
        List<HCEHealthConditionBo> result = resultQuery.stream()
                .map(HCEHealthConditionBo::new)
                .filter(HCEHealthConditionBo::isActive)
                .peek(hcePersonalHistoryBo -> hcePersonalHistoryBo.setCanBeMarkAsError(this.canBeMarkAsError(hcePersonalHistoryBo)))
				.sorted(Comparator.comparing(HCEHealthConditionBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

	@Override
	public List<HCEHealthConditionBo> getSummaryProblemsByUser(Integer patientId, Integer userId) {
		log.debug("Input parameters -> patientId {}, userId {}", patientId, userId);
		List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getSummaryProblemsByUser(patientId, userId);
		List<HCEHealthConditionBo> result = resultQuery.stream()
				.map(HCEHealthConditionBo::new)
				.filter(HCEHealthConditionBo::isActive)
                .peek(hcePersonalHistoryBo -> hcePersonalHistoryBo.setCanBeMarkAsError(this.canBeMarkAsError(hcePersonalHistoryBo)))
				.sorted(Comparator.comparing(HCEHealthConditionBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
				.collect(Collectors.toList());
		log.debug(LOGGING_OUTPUT, result);
		return result;
	}

    @Override
    public List<HCEPersonalHistoryBo> getPersonalHistories(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEPersonalHistoryVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream()
                .map(HCEPersonalHistoryBo::new)
                .peek(personalHistory -> personalHistory.setProfessionalName(sharedPersonPort.getCompletePersonNameById(personalHistory.getProfessionalPersonId())))
                .collect(Collectors.toList());
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEHealthConditionBo> getFamilyHistories(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getFamilyHistories(patientId);
        List<HCEHealthConditionBo> result = resultQuery.stream().map(HCEHealthConditionBo::new).collect(Collectors.toList());
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEHealthConditionBo> getChronicConditions(Integer institutionId, Integer patientId) {
        log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getSummaryProblems(patientId);
        List<HCEHealthConditionBo> result = resultQuery.stream()
                .map(HCEHealthConditionBo::new)
                .filter(HCEHealthConditionBo::isChronic)
                .peek(hcePersonalHistoryBo -> hcePersonalHistoryBo.setCanBeMarkAsError(this.canBeMarkAsError(hcePersonalHistoryBo)))
                .sorted(Comparator.comparing(HCEHealthConditionBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
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
    public List<HCEHealthConditionBo> getActiveProblems(Integer institutionId, Integer patientId) {
        log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getSummaryProblems(patientId);
        List<HCEHealthConditionBo> result = resultQuery.stream().map(HCEHealthConditionBo::new)
                .filter(HCEHealthConditionBo::isActiveProblem)
                .peek(hcePersonalHistoryBo -> hcePersonalHistoryBo.setCanBeMarkAsError(this.canBeMarkAsError(hcePersonalHistoryBo)))
                .sorted(Comparator.comparing(HCEHealthConditionBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
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
    public List<HCEHealthConditionBo> getSolvedProblems(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getSummaryProblems(patientId);
        List<HCEHealthConditionBo> result = resultQuery.stream().map(HCEHealthConditionBo::new).filter(HCEHealthConditionBo::isSolvedProblem)
                .sorted(Comparator.comparing(HCEHealthConditionBo::getStartDate).reversed()).collect(Collectors.toList());
        log.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEHealthConditionBo> getProblemsAndChronicConditionsMarkedAsError(Integer patientId) {
        log.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionBo> result = hceHealthConditionRepository.getProblemsMarkedAsError(patientId).stream()
                .map(HCEHealthConditionBo::new)
                .sorted(Comparator.comparing(HCEHealthConditionBo::getStartDate).reversed())
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

    private Boolean canBeMarkAsError(HCEHealthConditionBo hceHealthConditionBo) {
        return isSameUserIdFromHealthCondition.run(hceHealthConditionBo.getId())
                && isWithinExpirationTimeLimit.run(hceHealthConditionBo.getId());
    }
}
