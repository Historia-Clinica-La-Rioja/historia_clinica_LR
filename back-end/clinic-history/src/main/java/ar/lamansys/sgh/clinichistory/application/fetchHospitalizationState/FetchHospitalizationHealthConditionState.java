package ar.lamansys.sgh.clinichistory.application.fetchHospitalizationState;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.HCHHealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.HealthConditionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FetchHospitalizationHealthConditionState {

    public static final String OUTPUT = "Output -> {}";

    private static final String LOGGING_INTERNMENT_EPISODE = "Input parameters -> internmentEpisodeId {}";

    private final HCHHealthConditionRepository hchHealthConditionRepository;

    public GeneralHealthConditionBo getGeneralState(Integer internmentEpisodeId) {
        log.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo result = new GeneralHealthConditionBo(data);
        log.debug(OUTPUT, result);
        return result;
    }

    public HealthConditionBo getMainDiagnosisGeneralState(Integer internmentEpisodeId) {
        log.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        HealthConditionBo result = generalHealthConditionBo.getMainDiagnosis();
        log.debug(OUTPUT, result);
        return result;
    }

    public List<DiagnosisBo> getAlternativeDiagnosisGeneralState(Integer internmentEpisodeId) {
        log.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<DiagnosisBo> result = generalHealthConditionBo.getDiagnosis();
        log.debug(OUTPUT, result);
        return result;
    }

    public List<DiagnosisBo> getActiveAlternativeDiagnosesGeneralState(Integer internmentEpisodeId) {
        log.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<DiagnosisBo> result = generalHealthConditionBo.getDiagnosis().stream().filter(DiagnosisBo::isActive).collect(Collectors.toList());
        log.debug(OUTPUT, result);
        return result;
    }

    public List<HealthConditionBo> getDiagnosesGeneralState(Integer internmentEpisodeId) {
        log.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<HealthConditionBo> result = new ArrayList<>();
        HealthConditionBo mainDiagnosis = generalHealthConditionBo.getMainDiagnosis();
        if (mainDiagnosis != null)
            result.add(mainDiagnosis);
        result.addAll(generalHealthConditionBo.getDiagnosis());
        log.debug(OUTPUT, result);
        return result;
    }


    public List<PersonalHistoryBo> getPersonalHistoriesGeneralState(Integer internmentEpisodeId) {
        log.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<PersonalHistoryBo> result = generalHealthConditionBo.getPersonalHistories().getContent();
        log.debug(OUTPUT, result);
        return result;
    }

    public List<FamilyHistoryBo> getFamilyHistoriesGeneralState(Integer internmentEpisodeId) {
        log.debug(LOGGING_INTERNMENT_EPISODE, internmentEpisodeId);
        List<HealthConditionVo> data = getGeneralStateData(internmentEpisodeId);
        GeneralHealthConditionBo generalHealthConditionBo = new GeneralHealthConditionBo(data);
        List<FamilyHistoryBo> result = generalHealthConditionBo.getFamilyHistories();
        log.debug(OUTPUT, result);
        return result;
    }

    private List<HealthConditionVo> getGeneralStateData(Integer internmentEpisodeId) {
        List<Short> invalidDocumentTypes = List.of(DocumentType.ANESTHETIC_REPORT);
        return hchHealthConditionRepository.findGeneralState(internmentEpisodeId, invalidDocumentTypes);
    }

}
