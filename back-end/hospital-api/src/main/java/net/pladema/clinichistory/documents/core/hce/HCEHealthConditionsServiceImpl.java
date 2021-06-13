package net.pladema.clinichistory.documents.core.hce;

import net.pladema.clinichistory.documents.repository.hce.HCEHealthConditionRepository;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEHealthConditionVo;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEHospitalizationVo;
import net.pladema.clinichistory.documents.service.hce.HCEHealthConditionsService;
import net.pladema.clinichistory.documents.service.hce.domain.HCEHospitalizationBo;
import net.pladema.clinichistory.documents.service.hce.domain.HCEPersonalHistoryBo;
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

    public HCEHealthConditionsServiceImpl(HCEHealthConditionRepository hceHealthConditionRepository) {
        this.hceHealthConditionRepository = hceHealthConditionRepository;
    }


    @Override
    public List<HCEPersonalHistoryBo> getActivePersonalHistories(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream()
                .map(HCEPersonalHistoryBo::new)
                .filter(HCEPersonalHistoryBo::isActive)
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
                .sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate).reversed()).collect(Collectors.toList());
        LOG.debug(LOGGING_OUTPUT, result);
        return result;
    }

    @Override
    public List<HCEPersonalHistoryBo> getActiveProblems(Integer patientId) {
        LOG.debug(LOGGING_INPUT, patientId);
        List<HCEHealthConditionVo> resultQuery = hceHealthConditionRepository.getPersonalHistories(patientId);
        List<HCEPersonalHistoryBo> result = resultQuery.stream().map(HCEPersonalHistoryBo::new).filter(HCEPersonalHistoryBo::isActiveProblem)
                .sorted(Comparator.comparing(HCEPersonalHistoryBo::getStartDate).reversed()).collect(Collectors.toList());
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
