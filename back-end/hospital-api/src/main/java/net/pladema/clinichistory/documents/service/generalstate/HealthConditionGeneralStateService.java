package net.pladema.clinichistory.documents.service.generalstate;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.GeneralHealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthHistoryConditionBo;

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
