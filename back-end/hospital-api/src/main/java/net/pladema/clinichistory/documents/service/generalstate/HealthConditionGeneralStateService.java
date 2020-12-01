package net.pladema.clinichistory.documents.service.generalstate;

import net.pladema.clinichistory.documents.service.ips.domain.DiagnosisBo;
import net.pladema.clinichistory.documents.service.ips.domain.GeneralHealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthHistoryConditionBo;

import java.util.List;

public interface HealthConditionGeneralStateService {

    GeneralHealthConditionBo getGeneralState(Integer internmentEpisodeId);

    HealthConditionBo getMainDiagnosisGeneralState(Integer internmentEpisodeId);

    List<DiagnosisBo> getAlternativeDiagnosisGeneralState(Integer internmentEpisodeId);

    List<HealthConditionBo> getDiagnosesGeneralState(Integer internmentEpisodeId);

    List<HealthHistoryConditionBo> getPersonalHistoriesGeneralState(Integer internmentEpisodeId);

    List<HealthHistoryConditionBo> getFamilyHistoriesGeneralState(Integer internmentEpisodeId);

    List<DiagnosisBo> getActiveAlternativeDiagnosesGeneralState(Integer internmentEpisodeId);
}
