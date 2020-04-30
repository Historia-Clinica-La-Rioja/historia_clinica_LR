package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.DiagnosisBo;
import net.pladema.internation.service.domain.ips.GeneralHealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthConditionBo;
import net.pladema.internation.service.domain.ips.HealthHistoryConditionBo;

import java.util.List;

public interface HealthConditionService {

    List<DiagnosisBo> loadDiagnosis(Integer patientId, Long documentId, List<DiagnosisBo> diagnosis);

    List<HealthHistoryConditionBo> loadPersonalHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> personalHistories);

    List<HealthHistoryConditionBo> loadFamilyHistories(Integer patientId, Long documentId, List<HealthHistoryConditionBo> familyHistories);

    GeneralHealthConditionBo getGeneralState(Integer internmentEpisodeId);

    List<HealthConditionBo> getDiagnosisGeneralState(Integer internmentEpisodeId);

    List<HealthHistoryConditionBo> getPersonalHistoriesGeneralState(Integer internmentEpisodeId);

    List<HealthHistoryConditionBo> getFamilyHistoriesGeneralState(Integer internmentEpisodeId);

}
