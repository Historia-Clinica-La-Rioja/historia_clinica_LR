package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.application.ports.HCEReferenceCounterReferenceStorage;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEReferenceProblemBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.HCEHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHealthConditionVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEHospitalizationVo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEHospitalizationBo;
import ar.lamansys.sgh.clinichistory.domain.hce.HCEPersonalHistoryBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HCEHealthConditionsServiceImpl implements HCEHealthConditionsService {

    private static final Logger LOG = LoggerFactory.getLogger(HCEHealthConditionsServiceImpl.class);

    private static final String LOGGING_OUTPUT = "Output -> {}";
    private static final String LOGGING_INPUT = "Input parameters -> patientId {} ";

    private final HCEHealthConditionRepository hceHealthConditionRepository;
    private final HCEReferenceCounterReferenceStorage hceReferenceCounterReferenceStorage;

    public HCEHealthConditionsServiceImpl(HCEHealthConditionRepository hceHealthConditionRepository,
                                          HCEReferenceCounterReferenceStorage hceReferenceCounterReferenceStorage) {
        this.hceHealthConditionRepository = hceHealthConditionRepository;
        this.hceReferenceCounterReferenceStorage = hceReferenceCounterReferenceStorage;
    }


    @Override
    public List<HCEPersonalHistoryBo> getActivePersonalHistories(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream()
                .map(HCEPersonalHistoryBo::new)
                .filter(HCEPersonalHistoryBo::isActive)
				.sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getFamilyHistories(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getFamilyHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getChronicConditions(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new).filter(HCEPersonalHistoryBo::isChronic)
                .sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed()).collect(Collectors.toList());

        List<HCEReferenceProblemBo> problemsWithReferences = hceReferenceCounterReferenceStorage.getProblemsWithReferences(patientId);
        result.stream().forEach(p -> {
            problemsWithReferences.stream().forEach(pwr -> {
                if (p.getSnomedSctid().equals(pwr.getSnomedSctid()) && p.getSnomedPt().equals(pwr.getSnomedPt()))
                    p.setHasPendingReference(true);
            });
        });

        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getActiveProblems(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new).filter(HCEPersonalHistoryBo::isActiveProblem)
                .sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder())).reversed()).collect(Collectors.toList());

        List<HCEReferenceProblemBo> problemsWithReferences = hceReferenceCounterReferenceStorage.getProblemsWithReferences(patientId);
        result.stream().forEach(p -> {
            problemsWithReferences.stream().forEach(pwr -> {
                if (p.getSnomedSctid().equals(pwr.getSnomedSctid()) && p.getSnomedPt().equals(pwr.getSnomedPt()))
                    p.setHasPendingReference(true);
            });
        });

        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getSolvedProblems(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new).filter(HCEPersonalHistoryBo::isSolvedProblem)
                .sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate).reversed()).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEHospitalizationBo> getHospitalizationHistory(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHospitalizationVo> resultQuery = hceHealthConditionRepository.getHospitalizationHistory(patientId);
        List<HCEHospitalizationBo> result = resultQuery.stream().map(HCEHospitalizationBo::new).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }
}
