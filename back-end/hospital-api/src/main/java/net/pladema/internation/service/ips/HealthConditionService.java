package net.pladema.internation.service.ips;

import net.pladema.internation.service.ips.domain.DiagnosisBo;
import net.pladema.internation.service.ips.domain.GeneralHealthConditionBo;
import net.pladema.internation.service.ips.domain.HealthConditionBo;
import net.pladema.internation.service.ips.domain.HealthHistoryConditionBo;

import java.util.List;
import java.util.Optional;

public interface HealthConditionService {

    HealthConditionBo loadMainDiagnosis(Integer patientId, Long documentId, Optional<HealthConditionBo> mainDiagnosis);

    List<DiagnosisBo> loadDiagnosis(Integer patientId, Long documentId, List<DiagnosisBo> diagnosis);

    List<HealthHistoryConditionBo> loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> personalHistories);

    List<HealthHistoryConditionBo> loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> familyHistories);

    GeneralHealthConditionBo getGeneralState(Integer internmentEpisodeId);

    HealthConditionBo getMainDiagnosisGeneralState(Integer internmentEpisodeId);

    List<DiagnosisBo> getDiagnosisGeneralState(Integer internmentEpisodeId);

    List<HealthHistoryConditionBo> getPersonalHistoriesGeneralState(Integer internmentEpisodeId);

    List<HealthHistoryConditionBo> getFamilyHistoriesGeneralState(Integer internmentEpisodeId);

    List<Integer> copyDiagnoses(List<Integer> diagnosesId);
}
