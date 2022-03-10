package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.HCHHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthHistoryConditionBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FetchHospitalizationHealthConditionState {

    private static final Logger LOG = LoggerFactory.getLogger(FetchHospitalizationHealthConditionState.class);

    public static final String OUTPUT = "Output -> {}";

    private static final String LOGGING_INTERNMENT_EPISODE = "Input parameters -> internmentEpisodeId {}";

    private final HCHHealthConditionRepository hchHealthConditionRepository;

    public FetchHospitalizationHealthConditionState(HCHHealthConditionRepository hchHealthConditionRepository){
        this.hchHealthConditionRepository = hchHealthConditionRepository;
    }

    public GeneralHealthConditionBo getGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo result = new GeneralHealthConditionBo(data);
        LOG.debug(OUTPUT, result);
        return result;
    }

    public HealthConditionBo getMainDiagnosisGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        HealthConditionBo result =  generalHealthConditionBo.getMainDiagnosis();
        LOG.debug(OUTPUT, result);
        return result;
    }

    public List<DiagnosisBo> getAlternativeDiagnosisGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
		List<DiagnosisBo> result =  generalHealthConditionBo.getDiagnosis();
        LOG.debug(OUTPUT, result);
        return result;
    }

    public List<DiagnosisBo> getActiveAlternativeDiagnosesGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
		List<DiagnosisBo> result =  generalHealthConditionBo.getDiagnosis().stream().filter(DiagnosisBo::isActive).collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    public List<HealthConditionBo> getDiagnosesGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<HealthConditionBo> result =  new ArrayList<>();
        HealthConditionBo mainDiagnosis =  generalHealthConditionBo.getMainDiagnosis();
        if (mainDiagnosis != null)
            result.add(mainDiagnosis);
        result.addAll(generalHealthConditionBo.getDiagnosis());
        LOG.debug(OUTPUT, result);
        return result;
    }


    public List<HealthHistoryConditionBo> getPersonalHistoriesGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<HealthHistoryConditionBo> result =  generalHealthConditionBo.getPersonalHistories();
        LOG.debug(OUTPUT, result);
        return result;
    }

    public List<HealthHistoryConditionBo> getFamilyHistoriesGeneralState(Integer internmentEpisodeId) {
        LOG.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<HealthHistoryConditionBo> result =  generalHealthConditionBo.getFamilyHistories();
        LOG.debug(OUTPUT, result);
        return result;
    }

    private List<HealthConditionVo> getGeneralStateData(Integer internmentEpisodeId) {
        return hchHealthConditionRepository.findGeneralState(internmentEpisodeId);
    }

}
