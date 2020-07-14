package net.pladema.clinichistory.hospitalization.service.generalstate;

import net.pladema.clinichistory.ips.service.domain.DiagnosisBo;
import net.pladema.clinichistory.ips.service.domain.GeneralHealthConditionBo;
import net.pladema.clinichistory.ips.service.domain.HealthConditionBo;
import net.pladema.clinichistory.ips.service.domain.HealthHistoryConditionBo;

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
