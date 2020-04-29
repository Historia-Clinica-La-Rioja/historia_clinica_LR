package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.GeneralHealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryCondition;

import java.util.List;

public interface HealthConditionService {

    List<HealthConditionBo> loadDiagnosis(Integer patientId, Long documentId, List<HealthConditionBo> diagnosis);

    List<HealthHistoryCondition> loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> personalHistories);

    List<HealthHistoryCondition> loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryCondition> familyHistories);

    GeneralHealthConditionBo getGeneralState(Integer internmentEpisodeId);

    List<HealthConditionBo> getDiagnosisGeneralState(Integer internmentEpisodeId);

    List<HealthHistoryCondition> getPersonalHistoriesGeneralState(Integer internmentEpisodeId);

    List<HealthHistoryCondition> getFamilyHistoriesGeneralState(Integer internmentEpisodeId);

}
